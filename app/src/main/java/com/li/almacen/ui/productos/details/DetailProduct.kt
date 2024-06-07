package com.li.almacen.ui.productos.details

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.li.almacen.R
import com.li.almacen.databinding.ActivityDetailProductBinding

class DetailProduct : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private var toolbar: Toolbar? = null
    private var isFormModified = false

    private var uri : Uri? = null
    private var newUri : Uri? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { ur ->
        if (ur != null) {
            binding.imgPicker.setImageURI(ur)
            uri = ur
            newUri = ur
        }
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initData()
        initActions()
    }

    private fun initData() {
        val nameProd = intent.getStringExtra("nameProd") ?: ""
        val barcodeProd = intent.getStringExtra("barcodeProd") ?: ""
        val almacenProd = intent.getStringExtra("almacenProd") ?: ""
        val categoriaProd = intent.getStringExtra("categoriaProd") ?: ""
        val proveedorProd = intent.getStringExtra("proveedorProd") ?: ""
        val cantidadProd = intent.getStringExtra("cantidadProd") ?: ""
        val costeProd = intent.getStringExtra("costeProd") ?: ""
        val ventaProd = intent.getStringExtra("ventaProd") ?: ""
        val descripProd = intent.getStringExtra("descripProd") ?: ""
        val fechaVencimientoProd = intent.getStringExtra("fechaVencimientoProd") ?: ""

        val editableNameProd = Editable.Factory.getInstance().newEditable(nameProd)
        val editableBarcodeProd = Editable.Factory.getInstance().newEditable(barcodeProd)
        val editableAlmacenProd = Editable.Factory.getInstance().newEditable(almacenProd)
        val editableCategoriaProd = Editable.Factory.getInstance().newEditable(categoriaProd)
        val editableProveedorProd = Editable.Factory.getInstance().newEditable(proveedorProd)
        val editableCantidadProd = Editable.Factory.getInstance().newEditable(cantidadProd)
        val editableCosteProd = Editable.Factory.getInstance().newEditable(costeProd)
        val editableVentaProd = Editable.Factory.getInstance().newEditable(ventaProd)
        val editableDescripProd = Editable.Factory.getInstance().newEditable(descripProd)
        val editableFechaVencimientoProd = Editable.Factory.getInstance().newEditable(fechaVencimientoProd)

        binding.editName.text = editableNameProd
        binding.editBarcode.text = editableBarcodeProd
        binding.editAlmacen.text = editableAlmacenProd
        binding.editCategoria.text = editableCategoriaProd
        binding.editProveedor.text = editableProveedorProd
        binding.editCantidad.text = editableCantidadProd
        binding.editCoste.text = editableCosteProd
        binding.editVenta.text = editableVentaProd
        binding.editDescription.text = editableDescripProd
        binding.editFecha.text = editableFechaVencimientoProd

    }

    private fun initToolbar() {
        toolbar = binding.toolbar
        toolbar?.setNavigationOnClickListener { this@DetailProduct.finish()}
        toolbar?.inflateMenu(R.menu.example_dialog)
        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSave -> {
                    confirmDialogBuilder()
                    true
                }
                else -> {
                    this@DetailProduct.finish()
                    false
                }
            }
        }
    }

    private fun initActions() {
        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun confirmDialogBuilder() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this@DetailProduct)
        builder.setMessage("¿Deseas guardar los cambios?")
            .setTitle("Guardar cambios")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }


    private fun validateEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { isFormModified = true }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
                    else -> null
                }
            }
        })
    }

    private fun validateNumericEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { isFormModified = true }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
                    text.toInt() <= 0 -> "No se permiten números inferior a 0."
                    !text.matches(Regex("\\d+")) -> "Solo se permiten números."
                    else -> null
                }
            }
        })
    }

    private fun validatePriceEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { isFormModified = true }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
                    text.toInt() <= 0 -> "No se permiten números inferior a 0."
                    !text.matches(Regex("\\d+(\\.\\d{1,2})?")) -> "Formato inválido. Solo se permiten números y hasta dos decimales."
                    else -> null
                }
            }
        })
    }

    private fun validateEditTextBarcode(layout: TextInputLayout, edit: TextInputEditText) {
        val errorMessage = "Este campo debe ser un código de barras válido de 13 dígitos."

        fun validate(): Boolean {
            val text = edit.text.toString()
            return when {
                text.isEmpty() -> {
                    layout.error = "Este campo es obligatorio."
                    false
                }
                text.length != 13 -> {
                    layout.error = errorMessage
                    false
                }
                !text.all { it.isDigit() } -> {
                    layout.error = errorMessage
                    false
                }
                else -> {
                    layout.error = null
                    true
                }
            }
        }

        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { isFormModified = true }

            override fun afterTextChanged(s: Editable?) {
                validate()
            }
        })

        edit.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate()
            }
        }
    }

    private fun setUpNextField(edit: TextInputEditText, nextEdit: TextInputEditText?) {
        edit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                nextEdit?.requestFocus()
                true
            } else {
                false
            }
        }
    }

    private fun nextTextField() {
        setUpNextField(binding.editName, binding.editBarcode)
        setUpNextField(binding.editBarcode, binding.editCantidad)
        setUpNextField(binding.editCantidad, binding.editCoste)
        setUpNextField(binding.editCoste, binding.editVenta)
        setUpNextField(binding.editVenta, binding.editFecha)
        setUpNextField(binding.editFecha, null)
    }

    private fun formModified() {
        if (isFormModified) {
            AlertDialog.Builder(this@DetailProduct)
                .setTitle("Atención")
                .setMessage("Deseas  salir sin guardar los cambios?")
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Confirmar") { dialog, _ ->
                    dialog.dismiss()
                    this@DetailProduct.finish()
                }
                .show()
        } else {
            this@DetailProduct.finish()
        }
    }
}