package com.li.almacen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.li.almacen.cusdialog.ExampleDialog
import com.li.almacen.databinding.ActivityPruebasBinding


class ActivityPruebas : AppCompatActivity() {
    lateinit var binding: ActivityPruebasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPruebasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testButton.setOnClickListener { v -> openDialog() }
    }

    private fun openDialog() {
        ExampleDialog.display(supportFragmentManager)

    }
}