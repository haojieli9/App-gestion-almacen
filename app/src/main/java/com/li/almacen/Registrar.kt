package com.li.almacen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    }
}