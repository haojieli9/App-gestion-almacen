package com.li.almacen.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.databinding.ActivityRegistrarBinding
import com.li.almacen.ui.login.Login
import java.util.regex.Pattern


class Registrar : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


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
        validatePwd(R.id.tilPassword, R.id.regPassword1)
        confirmPwd(R.id.tilRepeatPassword, R.id.regPassword2, R.id.regPassword1)
        validateEditText(R.id.tilNickname, R.id.regUsername)
        validateEmail(R.id.tilEmail, R.id.regEmail)

    }

    private fun setup() {
        binding.regButton.setOnClickListener {
            if (binding.regEmail.text!!.isNotEmpty() && binding.regPassword1.text!!.isNotEmpty()
                && binding.regPassword2.text!!.isNotEmpty() && binding.regUsername.text!!.isNotEmpty()
                && binding.regPassword1.text.toString() == binding.regPassword2.text.toString())
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.regEmail.text.toString(), binding.regPassword1.text.toString()
                ).addOnCompleteListener { registrationTask ->
                    if (registrationTask.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { emailVerificationTask ->
                                if (emailVerificationTask.isSuccessful) {
                                    // El correo de verificación se envió correctamente
                                    Toast.makeText(this, "Usuario registrado correctamente. Se ha enviado un correo de verificación.", Toast.LENGTH_SHORT).show()

                                    // Aquí puedes iniciar la actividad de validación o realizar otras acciones
                                    val intent = Intent(this, RegValidar::class.java)
                                    intent.putExtra("email", binding.regEmail.text.toString())
                                    startActivity(intent)
                                    this@Registrar.finish()
                                } else {
                                    // No se pudo enviar el correo de verificación
                                    Toast.makeText(this, "Error al enviar el correo de verificación.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // Error al registrar el usuario
                        binding.tilEmail.endIconMode = TextInputLayout.END_ICON_NONE
                        binding.regEmail.error = "Este correo ya está en registro."
                        Toast.makeText(this, "Error al registrar usuario, compruebe los datos estén correctos.", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(this, "Error al registrar usuario, compruebe los datos esten correctos.", Toast.LENGTH_LONG).show()
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
                val containsUpperCase = text.matches(Regex(".*[A-Z].*"))
                val containsLetters = text.matches(Regex(".*[a-zA-Z].*"))

                if (text.isEmpty()) {
                    textInputLayout.error = "Este campo es obligatorio."
                } else {
                    textInputLayout.error = null
                    if (text.length < 8 || !containsUpperCase || !containsLetters) {
                        textInputLayout.error = "La contraseña debe tener al menos 8 caracteres y contener al menos un caracter mayúscula"
                    } else {
                        textInputLayout.error = null
                    }
                }
            }
        })
    }

    private fun confirmPwd(layout1: Int, edit1: Int, edit2: Int) {
        val inputLayout2 = findViewById<TextInputLayout>(layout1)
        val edittext2 = findViewById<TextInputEditText>(edit1)

        val edittext3 = findViewById<TextInputEditText>(edit2)

        edittext2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val containsUpperCase = text.matches(Regex(".*[A-Z].*"))
                val containsLetters = text.matches(Regex(".*[a-zA-Z].*"))

                if(text != edittext3.text.toString()){
                    inputLayout2.error = "Las contraseñas deben ser las mismas."
                } else {
                    inputLayout2.error = null
                    if (text.isEmpty()) {
                        inputLayout2.error = "Este campo es obligatorio."
                    } else {
                        inputLayout2.error = null
                        if (text.length < 8 || !containsUpperCase || !containsLetters) {
                            inputLayout2.error = "La contraseña debe tener al menos 8 caracteres y contener al menos un caracter mayúscula"
                        } else {
                            inputLayout2.error = null
                        }
                    }
                }
            }
        })
    }

    private fun validateEditText(layout: Int, edit: Int) {
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
                    if (text.length < 8) {
                        lay.error = "El nombre debe tener al menos 8 caracteres."
                    } else {
                        lay.error = null

                    }
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
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)\$"
        )
        return emailPattern.matcher(email).matches()
    }


}