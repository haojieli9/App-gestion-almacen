package com.li.almacen.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.li.almacen.R
import com.li.almacen.databinding.ActivityRegValidarBinding

class RegValidar : AppCompatActivity() {
    private lateinit var binding: ActivityRegValidarBinding
    private lateinit var auth: FirebaseAuth

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
            // Este bloque se ejecutará solo si el correo electrónico no está verificado
            Toast.makeText(this, "Por favor, verifique el correo de verificación antes de ser identificado.", Toast.LENGTH_LONG).show()
        }

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        val isEmailVerified: Boolean = currentUser?.isEmailVerified ?: false

        // Habilita o deshabilita el botón según el estado del correo electrónico verificado
        binding.buttonValidar.isEnabled = isEmailVerified

        // Establece el OnClickListener para el botón cuando el correo electrónico está verificado
        if (isEmailVerified) {
            binding.buttonValidar.setOnClickListener {
                this@RegValidar.finish()
                Toast.makeText(this, "El correo de verificación ha sido verificado correctamente.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
