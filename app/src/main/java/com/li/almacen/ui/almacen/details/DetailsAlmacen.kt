package com.li.almacen.ui.almacen.details

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.li.almacen.R
import com.li.almacen.databinding.ActivityDetailsAlmacenBinding

class DetailsAlmacen : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsAlmacenBinding
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initData()

    }

    private fun initData() {
        val id = intent.getStringExtra("id") ?: ""
        val nombre = intent.getStringExtra("name") ?: ""
        val descripcion = intent.getStringExtra("description") ?: ""
        val encargado = intent.getStringExtra("encargado") ?: ""
        val detailsUbi = intent.getStringExtra("ubicacion") ?: ""
//        val uri = intent.getStringExtra("uri")
        val fecha = intent.getStringExtra("fecha")

        val editableId = Editable.Factory.getInstance().newEditable(id)
        val editableNombre = Editable.Factory.getInstance().newEditable(nombre)
        val editableDescripcion = Editable.Factory.getInstance().newEditable(descripcion)
        val editableEncargado = Editable.Factory.getInstance().newEditable(encargado)
        val editableDetailsUbi = Editable.Factory.getInstance().newEditable(detailsUbi)
        val editableFecha = Editable.Factory.getInstance().newEditable(fecha)

        binding.formEditId.text = editableId
        binding.formEdit1.text = editableNombre
        binding.formEdit2.text = editableDescripcion
        binding.formEdit3.text = editableEncargado
        binding.formEdit5.text = editableDetailsUbi
        binding.formEditDate.text = editableFecha

        val uriString: String? = getUriString()
        if (!uriString.isNullOrEmpty()) {
            Log.d("DetailsAlmacen", "Loading image from URI: $uriString")
            val uri = Uri.parse(uriString)
            Glide.with(this)
                .load(uri)
                .error(R.drawable.png_file)
                .into(binding.detailImage)
        } else {
            Log.d("DetailsAlmacen", "URI is null or empty, using default image.")
        }
    }

    private fun initToolbar() {
        toolbar = binding.toolbar
        toolbar?.setNavigationOnClickListener { this@DetailsAlmacen.finish()}
        toolbar?.inflateMenu(R.menu.example_dialog)
        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSave -> {
                    confirmDialogBuilder()
                    true
                }
                else -> {
                    this@DetailsAlmacen.finish()
                    false
                }
            }
        }
    }

    private fun getUriString(): String? {
        return intent.getStringExtra("uri")
    }

    private fun validateEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
                    else -> null
                }
            }
        })
    }

    private fun confirmDialogBuilder() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DetailsAlmacen)
        builder.setMessage("¿Deseas guardar los cambios?")
            .setTitle("Guardar cambios")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}