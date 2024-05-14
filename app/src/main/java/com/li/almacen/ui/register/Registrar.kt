package com.li.almacen.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.li.almacen.R
import com.li.almacen.databinding.ActivityRegistrarBinding
import com.li.almacen.ui.login.Login


class Registrar : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.regTv3.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            this@Registrar.finish()
        }

        // Inicio
        setup()
        validateEditText()
    }

    private fun setup() {
        binding.regButton.setOnClickListener() {
            if (binding.regEmail.text!!.isNotEmpty() &&
                binding.regPassword1.text!!.isNotEmpty() &&
                binding.regPassword2.text!!.isNotEmpty() &&
                binding.regUsername.text!!.isNotEmpty() &&
                binding.regPassword1.text.toString() == binding.regPassword2.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.regEmail.text.toString(), binding.regPassword1.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, RegValidar::class.java)
                            intent.putExtra("email", binding.regEmail.text.toString())
                            startActivity(intent)
                            this@Registrar.finish()
                        } else {
                            Toast.makeText(this, "Error al registrar usuario, compruebe los datos esten correctos.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Error al registrar usuario, compruebe los datos esten correctos.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateEditText() {
        val textInputLayout = findViewById<TextInputLayout>(R.id.tilPassword)
        val editTextPassword = findViewById<TextInputEditText>(R.id.regPassword1)

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementación
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementación
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length < 8) {
                    textInputLayout.error = "La contraseña debe tener al menos 8 caracteres"
                    textInputLayout.isErrorEnabled = true
                } else {
                    textInputLayout.error = null
                    textInputLayout.isErrorEnabled = false
                }
            }
        })
    }

}