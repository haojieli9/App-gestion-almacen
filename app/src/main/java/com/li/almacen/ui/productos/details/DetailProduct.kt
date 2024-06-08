package com.li.almacen.ui.productos.details

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.CategoryData
import com.li.almacen.data.ProductData
import com.li.almacen.data.ProveedorData
import com.li.almacen.databinding.ActivityDetailProductBinding
import com.li.almacen.ui.productos.ProductViewModel

open class DetailProduct : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private var toolbar: Toolbar? = null
    private var isFormModified = false
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private val productViewModel : ProductViewModel by viewModels()

    var uri : Uri? = null
    var newUri : Uri? = null
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
        loadCategorias()
        loadProveedores()
        initData()
        initActions()
        componentValidation()
        nextTextField()

        val uriString: String? = getUriString2()
        if (!uriString.isNullOrEmpty()) {
            Log.d("DetailsAlmacen", "Loading image from URI: $uriString")
            val uri = Uri.parse(uriString)
            Glide.with(this)
                .load(uri)
                .error(R.drawable.png_file)
                .into(binding.imgPicker)
        } else {
            Log.d("DetailsAlmacen", "URI is null or empty, using default image.")
        }

    }

    override fun onBackPressed() {
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
                    super.onBackPressed()

                }
                .show()
        } else {
            this@DetailProduct.finish()
        }
    }

    fun getUriString2(): String? {
        return intent.getStringExtra("uriProd")
    }

    private fun componentValidation() {
        validateEditText(binding.tilName, binding.editName)
        validateEditTextBarcode(binding.tilBarcode, binding.editBarcode)
        validateNumericEditText(binding.tilCantidad, binding.editCantidad)
        validatePriceEditText(binding.tilCoste, binding.editCoste)
        validatePriceEditText(binding.tilVenta, binding.editVenta)

        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.editAlmacen.isEnabled = false
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

        val uriString: String? = getUriString()
        if (!uriString.isNullOrEmpty()) {
            Log.d("DetailsAlmacen", "Loading image from URI: $uriString")
            val uri = Uri.parse(uriString)
            Glide.with(this)
                .load(uri)
                .error(R.drawable.png_file)
                .into(binding.imgPicker)
        } else {
            Log.d("DetailsAlmacen", "URI is null or empty, using default image.")
        }
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DetailProduct)
        builder.setMessage("¿Deseas guardar los cambios?")
            .setTitle("Guardar cambios")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, _ ->
                val name = binding.editName.text.toString()
                val barcode = binding.editBarcode.text.toString()
                val cantidad = binding.editCantidad.text.toString()
                val coste = binding.editCoste.text.toString()
                val venta = binding.editVenta.text.toString()
                val descriptor = binding.editDescription.text.toString()

                when {
                    name.isEmpty() -> {
                        validateEditText(binding.tilName, binding.editName)
                    }
                    barcode.isEmpty() -> {
                        validateEditText(binding.tilBarcode, binding.editBarcode)
                    }
                    coste.isEmpty() -> {
                        validatePriceEditText(binding.tilCoste, binding.editCoste)
                    }
                    venta.isEmpty() -> {
                        validatePriceEditText(binding.tilVenta, binding.editVenta)
                    }
                    cantidad.isEmpty() -> {
                        validateNumericEditText(binding.tilCantidad, binding.editCantidad)
                    }
                    descriptor.isEmpty() -> {
                        validateEditText(binding.tilDescription, binding.editDescription)
                    }
                    else -> {
                        this@DetailProduct.finish()
                        dialog.dismiss()
                        guardarCambios()
                    }
                }
            }
        val dialog: AlertDialog = builder.create()
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

    private fun getUriString(): String? {
        return intent.getStringExtra("uri")
    }

    private fun guardarCambios() {
        val id = intent.getStringExtra("idProd")
        val name = binding.editName.text.toString()
        val barcode = binding.editBarcode.text.toString()
        val almacenDestino = binding.editAlmacen.text.toString()
        val categoria = binding.editCategoria.text.toString()
        val proveedor = binding.editProveedor.text.toString()
        val cantidad = binding.editCantidad.text.toString()
        val coste = binding.editCoste.text.toString()
        val venta = binding.editVenta.text.toString()
        val descriptor = binding.editDescription.text.toString()
        val fechaVencimiento = binding.editFecha.text.toString()
        updateProducto(id, name, barcode, almacenDestino, categoria, proveedor, cantidad, coste, venta, descriptor, fechaVencimiento)
    }

    private fun updateProducto(
        id: String?,
        name: String,
        barcode: String,
        almacenDestino: String,
        categoria: String,
        proveedor: String,
        cantidad: String,
        coste: String,
        venta: String,
        descriptor: String,
        fechaVencimiento: String,
    ) {
        // Verifica si el ID no es nulo
        id?.let { productId ->
            // Crea un nuevo objeto ProductoData con los datos actualizados
            val productoActualizado = ProductData(
                id = productId,
                name = name,
                barcode = barcode,
                almacenDestino = almacenDestino,
                categoria = categoria,
                proveedor = proveedor,
                cantidad = cantidad,
                coste = coste,
                venta = venta,
                descriptor = descriptor,
                fechaVencimiento = fechaVencimiento,
                newUri
            )

            // Actualiza los datos en Firestore
            db.collection("usuarios").document(userEmail!!).collection("productos")
                .document(productId)
                .set(productoActualizado)
                .addOnSuccessListener {
                    productViewModel.updateProducto(productoActualizado) // Suponiendo que tienes un método para actualizar en el ViewModel
                    Toast.makeText(
                        this@DetailProduct,
                        "Producto actualizado correctamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al actualizar producto - ProductoForm", e)
                    Toast.makeText(
                        this@DetailProduct,
                        "Error al actualizar producto.",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    private fun loadProveedores() {
        db.collection("usuarios").document(userEmail!!).collection("proveedores")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    ProveedorData(document.id, document.getString("nombre") ?: "")
                }
                val adapter = ArrayAdapter(this@DetailProduct, R.layout.dropdown_item1, almacenes.map { it.name })

                binding.editProveedor.setAdapter(adapter)
                binding.editProveedor.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(this@DetailProduct, "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadCategorias() {
        db.collection("usuarios").document(userEmail!!).collection("categorias")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    CategoryData(document.id, document.getString("name") ?: "")
                }
                val adapter = ArrayAdapter(this@DetailProduct, R.layout.dropdown_item1, almacenes.map { it.name })

                binding.editCategoria.setAdapter(adapter)
                binding.editCategoria.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(this@DetailProduct, "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

}