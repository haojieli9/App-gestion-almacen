package com.li.almacen

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.li.almacen.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.username.setText("li")
        binding.password.setText("1234")
        binding.checkLegacy.isChecked = true

        binding.loginButton.setOnClickListener {
            val nombreCorrecto = binding.username.text.toString() == "li"
            val pwdCorrecto = binding.password.text.toString() == "1234"

            if (nombreCorrecto && pwdCorrecto) {
                if (binding.checkLegacy.isChecked) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    this@Login.finish()
                } else {
                    Toast.makeText(this@Login, "Debes aceptar condiciones legales para continuar.", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (!nombreCorrecto) {
                    binding.username.setTextColor(Color.parseColor("#FF0000"))
                }
                if (!pwdCorrecto) {
                    binding.password.setTextColor(Color.parseColor("#FF0000"))
                }

                var mensajeError = "Error de autenticación. Aegurese de que los datos sean correctos."

                //Snackbar.make(binding.root, mensajeError, Snackbar.LENGTH_SHORT).show()

                val builder = AlertDialog.Builder(this@Login)
                builder.setMessage(mensajeError)
                builder.setPositiveButton("ACEPTAR") { dialog, _ -> dialog.dismiss() }
                val alertDialog = builder.create()
                alertDialog.show()

                /*Tipo de notiifcaciones
                    - Snackbar
                    - AlerDialog
                    - Toast
                    - Custom toast
                */
            }
        }
    }
}