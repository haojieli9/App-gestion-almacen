package com.li.almacen.usuarios

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.li.almacen.databinding.ActivityRegistrarBinding


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
    }

    private fun setup() {
        binding.regButton.setOnClickListener() {
/*
            if (binding.regEmail.text.isNotEmpty() &&
                binding.regPassword1.text.isNotEmpty() &&
                binding.regPassword2.text.isNotEmpty() &&
                binding.regUsername.text.isNotEmpty() &&
                binding.regPassword1.text.toString() == binding.regPassword2.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.regEmail.text.toString(), binding.regPassword1.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, RegValidar::class.java)
                            startActivity(intent)
                            this@Registrar.finish()
                        } else {
                            Toast.makeText(this, "Error al registrar usuario, compruebe los datos esten correctos.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Error al registrar usuario, compruebe los datos esten correctos.", Toast.LENGTH_LONG).show()
            }*/

            val intent = Intent(this, RegValidar::class.java)
            startActivity(intent)
            this@Registrar.finish()
/*
            val builder = MaterialAlertDialogBuilder(this)
                .setTitle("Título del diálogo")
                .setMessage("Mensaje del diálogo")
                .setPositiveButton("Aceptar") { dialog, which ->
                    // Manejar clic en el botón Aceptar
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    // Manejar clic en el botón Cancelar
                }

            // Crear y mostrar el diálogo
            val dialog = builder.create()
            dialog.show()
*/
        }
    }
}