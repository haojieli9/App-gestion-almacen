package com.li.almacen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.li.almacen.databinding.ActivityPruebasBinding

class ActivityPruebas : AppCompatActivity() {
    lateinit var binding: ActivityPruebasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPruebasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}