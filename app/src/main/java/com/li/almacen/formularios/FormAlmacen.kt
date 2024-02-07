package com.li.almacen.formularios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.li.almacen.apartamentos.ActivityAlmacen
import com.li.almacen.databinding.ActivityFormAlmacenBinding
import com.li.almacen.kt.Almacenes
import com.li.almacen.kt.CustomAdapter
import com.li.almacen.kt.CustomArticulo
import com.li.almacen.kt.listaAlmacen

class FormAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityFormAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.btGuardar.setOnClickListener {
            val tfID = binding.tfID
            val tfNombre = binding.tfNombre
            val tfDir = binding.tfDir
                listaAlmacen.add(Almacenes(tfID.text.toString(), tfNombre.text.toString(), tfDir.text.toString()))
            val intent = Intent(this@FormAlmacen, ActivityAlmacen::class.java)
            startActivity(intent)
            this@FormAlmacen.finish()
        }
    }
}