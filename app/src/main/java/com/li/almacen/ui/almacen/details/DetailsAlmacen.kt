package com.li.almacen.ui.almacen.details

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.li.almacen.R
import com.li.almacen.databinding.ActivityDetailsAlmacenBinding

class DetailsAlmacen : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

    }

    private fun initData() {
        val id = intent.getStringExtra("id") ?: ""
        val nombre = intent.getStringExtra("name") ?: ""
        val descripcion = intent.getStringExtra("description") ?: ""
        val encargado = intent.getStringExtra("encargado") ?: ""
        val capacidad = intent.getStringExtra("capacidad") ?: ""
        val detailsUbi = intent.getStringExtra("ubicacion") ?: ""
        val uri = intent.getStringExtra("uri")

        val editableId = Editable.Factory.getInstance().newEditable(id)
        val editableNombre = Editable.Factory.getInstance().newEditable(nombre)
        val editableDescripcion = Editable.Factory.getInstance().newEditable(descripcion)
        val editableEncargado = Editable.Factory.getInstance().newEditable(encargado)
        val editableCapacidad = Editable.Factory.getInstance().newEditable(capacidad)
        val editableDetailsUbi = Editable.Factory.getInstance().newEditable(detailsUbi)

        binding.detailID.text = editableId
        binding.detailName.text = editableNombre
        binding.detailNote.text = editableDescripcion
        binding.detailManager.text = editableEncargado
        binding.detailData.text = editableCapacidad
        binding.detailsUbi.text = editableDetailsUbi

        Glide.with(this).load(uri).into(binding.detailImage)
    }
}