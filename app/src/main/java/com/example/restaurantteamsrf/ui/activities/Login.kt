package com.example.restaurantteamsrf.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.TeamsDBApp
import com.example.restaurantteamsrf.application.TeamsDBApp.Companion.prefs
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.db.model.UserEntity
import com.example.restaurantteamsrf.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.UUID

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //SharedPreferences Encriptadas:
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var encryptedSharedPrefsEditor: SharedPreferences.Editor

    private var ususarioSp: String? = ""
    private var contraseniaSp: String? = ""

    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""

    private lateinit var mp: MediaPlayer

    private lateinit var repository: TeamRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserLoged()

        repository = (application as TeamsDBApp).repository


        mp = MediaPlayer.create(this, R.raw.impact)
        mp.start()

        firebaseAuth = FirebaseAuth.getInstance()

        try {
            //Creando la llave para encriptar
            val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedSharedPreferences = EncryptedSharedPreferences
                .create(
                    this,
                    "account",
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences
        }catch(e: GeneralSecurityException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }catch (e: IOException){
            e.printStackTrace()
            Log.d("LOGS", "Error: ${e.message}")
        }

        encryptedSharedPrefsEditor = encryptedSharedPreferences.edit()

        ususarioSp = encryptedSharedPreferences.getString("usuarioSp","0")
        contraseniaSp = encryptedSharedPreferences.getString("contraseniaSp", "0")


        binding.btnLogin.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //autenticando al usuario
            autenticaUsuario(email, contrasenia)
        }

        binding.btnRegistrarse.setOnClickListener {

            val intent = Intent(this, CreateOrJoin::class.java)
            startActivity(intent)
            finish()

        }

        binding.tvRestablecerPassword.setOnClickListener {
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(it.context)
                .setTitle("Restablecer contraseña")
                .setMessage("Ingrese su correo para recibir el enlace para restablecer")
                .setView(resetMail)
                .setPositiveButton("Enviar") { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "El enlace para restablecer la contraseña ha sido enviado a su correo",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "El enlace no se ha podido enviar: ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Favor de ingresar la dirección de correo",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }




}

    private fun validaCampos(): Boolean{
        email = binding.tietEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = "Se requiere el correo"
            binding.tietEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContrasenia.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, "Error: El correo electrónico no tiene un formato correcto", Toast.LENGTH_SHORT).show()
                binding.tietEmail.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = "La contraseña no es válida"
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.tietEmail.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.tietContrasenia.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContrasenia.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, "Red no disponible o se interrumpió la conexión", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticaUsuario(usr: String, psw: String){
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if(authResult.isSuccessful){
                Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                prefs.saveName(usr)

                    getIdentifierUser(usr, psw) { identificador ->
                    prefs.saveIDSesion(identificador)
                    Toast.makeText(this, usr, Toast.LENGTH_LONG).show()

                    val intent = Intent(this, ActivityMenu::class.java)
                    intent.putExtra("psw", psw)
                    startActivity(intent)
                    finish()
                }
            }else{
                binding.progressBar.visibility = View.GONE
                manejaErrores(authResult)
            }
        }
    }

    private fun checkUserLoged(){
        if(prefs.getName().isNotEmpty()){
            startActivity(Intent(this, ActivityMenu::class.java))
        }
    }

    private fun getIdentifierUser(usr:String, pass:String,callback: (String) -> Unit): String{
        var user = UserEntity()
        var identificador = ""

        lifecycleScope.launch {
            val currentUser = withContext(Dispatchers.IO) {
                repository.getCurrentUser(usr, pass)
            }

//            user = currentUser ?: UserEntity()
//            identificador = user.identificadorSesion

            val identificador = currentUser?.identificadorSesion ?: ""
            callback(identificador)

        }
        //Toast.makeText(this, identificador, Toast.LENGTH_LONG).show()

        return identificador
    }

//    fun generarIdentificadorUnico(): String {
//        return UUID.randomUUID().toString()
//    }

}