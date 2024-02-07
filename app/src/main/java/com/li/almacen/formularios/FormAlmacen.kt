package com.li.almacen.formularios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.li.almacen.R
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.third_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemIntentBack -> {
                val intent = Intent(this, ActivityAlmacen::class.java)
                startActivity(intent)
                this@FormAlmacen.finish()
                true
            } else -> { true }
        }
    }

}