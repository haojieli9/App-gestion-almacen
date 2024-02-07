package com.li.almacen.apartamentos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.li.almacen.R
import com.li.almacen.kt.CustomAdapter
import com.li.almacen.kt.listaAlmacen
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.formularios.FormAlmacen
import com.li.almacen.kt.listaArticulo

class ActivityAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rvAlma.adapter = CustomAdapter(listaAlmacen)
        binding.rvAlma.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.imgAdd.setOnClickListener {
            val intent = Intent(this, FormAlmacen::class.java)
            startActivity(intent)
        }

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