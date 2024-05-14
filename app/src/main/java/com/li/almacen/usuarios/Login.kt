package com.li.almacen.usuarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.li.almacen.MainActivity
import com.li.almacen.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configura el OnClickListener para el botón de inicio de sesión
        binding.loginButton.setOnClickListener {
            if (binding.username.text.isEmpty() || binding.password.text.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Obtiene los datos de inicio de sesión
                val email = binding.username.text.toString()
                val password = binding.password.text.toString()
                signIn(email, password)
            }
/*            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)*/
        }

        binding.logTv3.setOnClickListener() {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al iniciar sesión, compruebe los datos rellenados.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
