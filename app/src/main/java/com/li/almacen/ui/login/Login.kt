package com.li.almacen.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.li.almacen.R
import com.li.almacen.ui.MainActivity
import com.li.almacen.databinding.ActivityLoginBinding
import com.li.almacen.ui.register.Registrar
import java.util.regex.Pattern

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // PRUEBAS
        binding.email.setText("1@1.com")
        binding.password.setText("123456")


        // Configura el OnClickListener para el botón de inicio de sesión
        binding.loginButton.setOnClickListener {
            if (binding.email.text!!.isEmpty() || binding.password.text!!.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Obtiene los datos de inicio de sesión
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                signIn(email, password)
            }
        }

        binding.logTv3.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
            this@Login.finish()
        }

        validateEmail(R.id.tilEmail, R.id.email)
        validatePwd(R.id.tilPassword, R.id.password)
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Sesión iniciada correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al iniciar sesión, intente de nuevo.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun validatePwd(layout1: Int, edit1: Int) {
        val textInputLayout = findViewById<TextInputLayout>(layout1)
        val textInputEditText = findViewById<TextInputEditText>(edit1)


        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isEmpty()) {
                    textInputLayout.error = "Este campo es obligatorio."
                } else {
                    textInputLayout.error = null
                }
            }
        })
    }

    private fun validateEmail(layout: Int, edit: Int) {
        val lay = findViewById<TextInputLayout>(layout)
        val editt = findViewById<TextInputEditText>(edit)

        editt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isEmpty()) {
                    lay.error = "Este campo es obligatorio."
                } else {
                    lay.error = null
                    if (!isValidEmail(text)) {
                        lay.error = "Correo electrónico no válido."
                    } else {
                        lay.error = null
                    }
                }
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)\$"
        )
        return emailPattern.matcher(email).matches()
    }



}
