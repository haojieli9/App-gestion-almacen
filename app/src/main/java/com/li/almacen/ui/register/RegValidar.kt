package com.li.almacen.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.li.almacen.R
import com.li.almacen.databinding.ActivityRegValidarBinding

class RegValidar : AppCompatActivity() {
    private lateinit var binding: ActivityRegValidarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegValidarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra("email")
        binding.valTV1.text = email

        binding.buttonValidar.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                currentUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (currentUser.isEmailVerified) {
                            Toast.makeText(this, "El correo de verificación ha sido verificado correctamente", Toast.LENGTH_SHORT).show()
                            this@RegValidar.finish()
                        } else {
                            Toast.makeText(this, "El correo de verificación no ha sido verificado. Por favor, revisa en el correo de verificación.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Error al verificar el correo de verificación. Por favor, intenta de nuevo.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "No hay usuario actualmente autenticado. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
