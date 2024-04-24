package com.li.almacen.apartamentos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.li.almacen.R
import com.li.almacen.kt.CustomAdapter
import com.li.almacen.kt.listaAlmacen
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.formularios.FormAlmacen
import com.li.almacen.kt.Almacenes
import com.li.almacen.kt.listaArticulo

class ActivityAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicio toolbar
        setSupportActionBar(binding.toolbar)

        //inicio recyclerview
        val adapter = CustomAdapter(listaAlmacen)
        binding.rvAlma.adapter = adapter
        binding.rvAlma.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.setOnClickListener() { _: Almacenes, _: Int ->
            Toast.makeText(this@ActivityAlmacen, "Clicked", Toast.LENGTH_SHORT).show()
        }

        //boton intent al formulario
        binding.imgAdd.setOnClickListener {
            val intent = Intent(this, FormAlmacen::class.java)
            startActivity(intent)
            this@ActivityAlmacen.finish()
        }

        //cardview informacion general
        val cantidadAlmacen = listaAlmacen.size
        binding.tvCantAlm.text = cantidadAlmacen.toString()

        val totalArticulo = listaArticulo.size
        binding.tvUnitArt.text = totalArticulo.toString() + " units"

        val totalValue = listaArticulo.sumOf { it.precio.toDouble() }.toFloat()
        binding.tvValor.text = totalValue.toString() + "€"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemBack -> {
                finish()
                true
            }
            else -> { true }
        }
    }
}