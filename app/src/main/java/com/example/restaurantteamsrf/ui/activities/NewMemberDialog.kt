package com.example.restaurantteamsrf.ui.activities



import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.ActivityNewMemberDialogBinding
import com.example.restaurantteamsrf.databinding.ActivityNewTeamDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class NewMemberDialog (
    private var team: TeamEntity,
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {

    private var _binding: ActivityNewMemberDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog

    private var newUser = UserEntity()

    private var saveButton: Button? = null

    private lateinit var repository: TeamRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        _binding = ActivityNewMemberDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as TeamsDBApp). repository


        dialog = buildDialog("Guardar","Cancelar", {


            //Create (Guardar)
            val nombre = binding.tietMemberName.text.toString()
            newUser.name = nombre

            //Toast.makeText(requireContext(), "QUERIENDO AGREGAR ${newUser.name} a ${team.name}", Toast.LENGTH_LONG).show()

            team.members = team.members.plus(newUser)
                //Toast.makeText(requireContext(), "miembros: ${team.members.size}", Toast.LENGTH_LONG).show()
            try {
                lifecycleScope.launch {
                    repository.updateTeam(team)
                    repository.insertUser(newUser)
                    updateUI()
                }
                message("$nombre agregado exitosamente")
            }catch (e: IOException){
                e.printStackTrace()
                message("Error al añadir $nombre")
            }

        }, {
            //Cancelar
        })

        return dialog
    }

    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietMemberName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        return builder.setView(binding.root)
            .setTitle("New member")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener{ _, _ ->
                positiveButton()
            })
            .setNegativeButton(btn2Text){ _, _ ->
                negativeButton()
            }
            .create()
    }

    private fun validateFields() =
        (binding.tietMemberName.text.toString().isNotEmpty())

}
