package com.example.restaurantteamsrf.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.restaurantteamsrf.classes.Manager
import com.example.restaurantteamsrf.databinding.ActivityCreateManagerAccountBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class CreateManagerAccount : AppCompatActivity() {

    private lateinit var binding:ActivityCreateManagerAccountBinding

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
    private var nombre = ""
    private var apellido = ""
    private var numero = ""

    //Manager Objeto
    private lateinit var manager : Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateManagerAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnCrearCuenta.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            //binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    //Enviar correo para verificación de email
                    var user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, "No se pudo enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()

                    //Creacion del manager

                     nombre = binding.etNombre.text.toString()
                     apellido = binding.etNombre.text.toString()
                     numero = binding.etNombre.text.toString()

                    manager = Manager(nombre,apellido,numero,email,contrasenia)


                    val intent = Intent(this, CreateTeam::class.java)
                    intent.putExtra("manager", manager)
                    startActivity(intent)
                    finish()


                }else{
                    binding.progressBar.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

    }

    private fun validaCampos(): Boolean{
        email = binding.etCorreo.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.etContraseA.text.toString().trim()

        if(email.isEmpty()){
            binding.etCorreo.error = "Se requiere el correo"
            binding.etCorreo.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.etContraseA.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.etContraseA.requestFocus()
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
                binding.etCorreo.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.etCorreo.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.etContraseA.error = "La contraseña no es válida"
                binding.etContraseA.requestFocus()
                binding.etContraseA.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.etCorreo.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.etCorreo.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.etContraseA.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.etContraseA.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, "Red no disponible o se interrumpió la conexión", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}