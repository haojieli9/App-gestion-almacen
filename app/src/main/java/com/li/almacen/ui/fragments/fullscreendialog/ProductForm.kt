package com.li.almacen.ui.fragments.fullscreendialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.li.almacen.R
import com.li.almacen.adapter.DataPickerFragment
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.CategoryData
import com.li.almacen.data.ProductData
import com.li.almacen.data.ProveedorData
import com.li.almacen.databinding.FormProductBinding
import com.li.almacen.test.FormAlmacen
import com.li.almacen.ui.productos.ProductViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class ProductForm : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var toolbar: Toolbar? = null
    private var almacenid = mutableListOf<String?>()
    private var selectedAlmacenId: String? = ""
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var binding: FormProductBinding
    var uri : Uri? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { ur ->
        if (ur != null) {
            binding.imgPicker.setImageURI(ur)
            uri = ur
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
            it.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FormProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = binding.toolbar
        initActions()
        nextTextField()

        toolbar?.setNavigationOnClickListener { dismiss() }
        toolbar?.title = "Nuevo producto"
        toolbar?.inflateMenu(R.menu.example_dialog)
        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSave -> {
                    confirmDialogBuilder()
                    true
                }
                else -> {
                    dismiss()
                    false
                }
            }
        }

        // Component validation
        validateEditText(binding.tilName, binding.editName)
        validateEditTextBarcode(binding.tilBarcode, binding.editBarcode)
        validateNumericEditText(binding.tilCantidad, binding.editCantidad)
        validatePriceEditText(binding.tilCoste, binding.editCoste)
        validatePriceEditText(binding.tilVenta, binding.editVenta)

        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        loadAlmacenes()
        loadProveedores()
        loadCategorias()

        binding.editAlmacen.setOnItemClickListener { parent, view, position, id ->
                selectedAlmacenId = almacenid[position]
                Log.d("Selected Almacen", "ID: $selectedAlmacenId")
        }
    }

    companion object {
        private const val TAG = "FORMALMACEN"
        fun display(fragmentManager: FragmentManager?): ProductForm {
            val productdialog = ProductForm()
            productdialog.show(fragmentManager!!, TAG)
            return productdialog
        }
    }

    private fun confirmDialogBuilder() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Deseas guardar este producto?")
            .setTitle("Guardar cambios")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, _ ->

                val name = binding.editName.text.toString()
                val almacen = binding.editAlmacen.text.toString()
                val categoria = binding.editCategoria.text.toString()
                val proveedor = binding.editProveedor.text.toString()
                val cantidad = binding.editCantidad.text.toString()
                val coste = binding.editCoste.text.toString()
                val venta = binding.editVenta.text.toString()
                val description = binding.editDescription.text.toString()

                when {
                    name.isEmpty() -> {
                        binding.tilName.error = "Este campo es obligatorio."
                    }
                    almacen.isEmpty() -> {
                        binding.tilBarcode.error = "Este campo es obligatorio."
                    }
                    categoria.isEmpty() -> {
                        binding.tilCategoria.error = "Este campo es obligatorio."
                    }
                    proveedor.isEmpty() -> {
                        binding.tilProveedor.error = "Este campo es obligatorio."
                    }
                    cantidad.isEmpty() -> {
                        binding.tilCantidad.error = "Este campo es obligatorio."
                    }
                    coste.isEmpty() -> {
                        binding.tilCoste.error = "Este campo es obligatorio."
                    }
                    venta.isEmpty() -> {
                        binding.tilVenta.error = "Este campo es obligatorio."
                    }
                    description.isEmpty() -> {
                        binding.tilDescription.error = "Este campo es obligatorio."
                    }
                    else -> {
                        saveProduct()
                        dialog.dismiss()
                        this@ProductForm.dismiss()
                    }
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun loadAlmacenes() {
        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    AlmacenData(document.id, document.getString("name") ?: "")
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item1, almacenes.map { it.name })
                almacenid = almacenes.map { it.id }.toMutableList()

                binding.editAlmacen.setAdapter(adapter)
                binding.editAlmacen.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(requireContext(), "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadProveedores() {
        db.collection("usuarios").document(userEmail!!).collection("proveedores")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    ProveedorData(document.id, document.getString("nombre") ?: "")
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item1, almacenes.map { it.name })

                binding.editProveedor.setAdapter(adapter)
                binding.editProveedor.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(requireContext(), "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

    private fun loadCategorias() {
        db.collection("usuarios").document(userEmail!!).collection("categorias")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    CategoryData(document.id, document.getString("name") ?: "")
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item1, almacenes.map { it.name })

                binding.editCategoria.setAdapter(adapter)
                binding.editCategoria.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(requireContext(), "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveProduct() {
        if (!isAdded) return

        val name = binding.editName.text.toString()
        val barcode = binding.editBarcode.text.toString()
        val almacen = binding.editAlmacen.text.toString()
        val categoria = binding.editCategoria.text.toString()
        val proveedor = binding.editProveedor.text.toString()
        val cantidad = binding.editCantidad.text.toString()
        val coste = binding.editCoste.text.toString()
        val venta = binding.editVenta.text.toString()
        val descripcion = binding.editDescription.text.toString()
        val fecha = binding.editFecha.text.toString()

        val nuevoProducto = ProductData(null, name, barcode, almacen, categoria, proveedor, cantidad, coste, venta, descripcion, fecha, uri, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))

        db.collection("usuarios").document(userEmail!!).collection("productos")
            .add(nuevoProducto)
            .addOnSuccessListener { documentReference ->
                nuevoProducto.id = documentReference.id
                documentReference.update("id", nuevoProducto.id)
                    .addOnSuccessListener {
                        db.collection("usuarios").document(userEmail)
                            .collection("productos_almacenes")
                            .add(mapOf("productoId" to nuevoProducto.id, "almacenId" to selectedAlmacenId))
                            .addOnSuccessListener {
                                if (isAdded) {
                                    productViewModel.addProduct(nuevoProducto)
                                    Toast.makeText(requireContext(), "Producto registrado correctamente.", Toast.LENGTH_LONG).show()
                                    this@ProductForm.dismiss()
                                }
                            }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al agregar producto", e)
                Toast.makeText(requireContext(), "Error al registrar producto.", Toast.LENGTH_LONG).show()
            }
    }

    private fun initActions() {
        binding.tilBarcode.setEndIconOnClickListener {
            initScanner()
        }

        binding.tilBarcode.setOnClickListener {
            initScanner()
        }

        binding.editBarcode.setOnClickListener {
            initScanner()
        }

        binding.editFecha.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tilFecha.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("BARCODE SCANNER")
        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
                binding.editBarcode.setText(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DataPickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        val selectedDate = "$day/$month/$year"
        binding.editFecha.setText(selectedDate)
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

    private fun validateNumericEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
                    !text.matches(Regex("\\d+")) -> "Solo se permiten números."
                    else -> null
                }
            }
        })
    }

    private fun validatePriceEditText(layout: TextInputLayout, edit: TextInputEditText) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                layout.error = when {
                    text.isEmpty() -> "Este campo es obligatorio."
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

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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

    // Llama a esta función antes de intentar guardar los datos para validar el campo
    private fun isValidBarcode(edit: TextInputEditText): Boolean {
        val text = edit.text.toString()
        return text.isNotEmpty() && text.length == 13 && text.all { it.isDigit() }
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
}
