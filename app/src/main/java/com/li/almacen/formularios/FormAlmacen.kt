package com.li.almacen.formularios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.li.almacen.R
import com.li.almacen.databinding.ActivityFormAlmacenBinding

class FormAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityFormAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}