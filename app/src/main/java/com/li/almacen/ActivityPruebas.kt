package com.li.almacen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.li.almacen.databinding.ActivityPruebasBinding
import com.li.almacen.kt.CustomArticulo
import com.li.almacen.kt.listaArticulo

class ActivityPruebas : AppCompatActivity() {
    lateinit var binding: ActivityPruebasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPruebasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvArt.adapter = CustomArticulo(listaArticulo)
        binding.rvArt.layoutManager = LinearLayoutManager(this@ActivityPruebas, LinearLayoutManager.HORIZONTAL, false)
    }
}