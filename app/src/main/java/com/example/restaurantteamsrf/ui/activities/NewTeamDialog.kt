package com.example.restaurantteamsrf.ui.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.TeamEntity
import com.example.restaurantteamsrf.databinding.ActivityNewTeamDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class NewTeamDialog (
    private var team: TeamEntity,
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit,
): DialogFragment() {

    private var _binding: ActivityNewTeamDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: TeamRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        _binding = ActivityNewTeamDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as TeamsDBApp). repository


        dialog = buildDialog("Guardar","Cancelar", {
                //Create (Guardar)
                team.name = binding.tietTitle.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.insertTeam(team)
                        updateUI()
                    }
                    message("Equipo creado exitosamente")
                }catch (e: IOException){
                    e.printStackTrace()
                    message("Error al crear el equipo")
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

        binding.tietTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

//        binding.tietGenre.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                saveButton?.isEnabled = validateFields()
//            }
//
//        })
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
            .setTitle("New team")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener{ _, _ ->
                positiveButton()
            })
            .setNegativeButton(btn2Text){ _, _ ->
                negativeButton()
            }
            .create()
    }

//    private fun validateFields() =
//        (binding.tietTitle.text.toString().isNotEmpty() &&
//                binding.tietGenre.text.toString().isNotEmpty())

        private fun validateFields() =
        (binding.tietTitle.text.toString().isNotEmpty())

}