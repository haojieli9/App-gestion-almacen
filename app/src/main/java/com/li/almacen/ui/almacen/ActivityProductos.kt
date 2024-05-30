package com.li.almacen.ui.almacen

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.li.almacen.R
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.databinding.ActivityProductosBinding
import com.li.almacen.kt.ProductAdapter
import com.li.almacen.kt.listaArticulo

class ActivityProductos : AppCompatActivity() {
    private lateinit var binding : ActivityProductosBinding
    private var adaptador = ProductAdapter(listaArticulo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rvProduct.adapter = adaptador
        binding.rvProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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