package com.li.almacen.ui.fragments.fullscreendialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.FormProductBinding

class ProductForm : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var toolbar: Toolbar? = null
    private var almacenid = mutableListOf<String?>()
    private var selectedAlmacenId: String? = ""
    private lateinit var binding: FormProductBinding


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imgPicker.setImageURI(uri)
            Log.d("URI", uri.toString())
        } else {
            // no hay imagen
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
        validateEditText(binding.formTil1, binding.formEdit1)

        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        loadAlmacenes()

        binding.formEdit2.setOnItemClickListener { parent, view, position, id ->
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
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.confirm_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.show()

        dialog.findViewById<Button>(R.id.testButton1).setOnClickListener {
            Toast.makeText(requireContext(), "Operación cancelada.", Toast.LENGTH_LONG).show()
            dialog.dismiss()
            dismiss()
        }

        dialog.findViewById<Button>(R.id.testButton2).setOnClickListener {
            saveProduct()
            dialog.dismiss()
        }
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

    private fun loadAlmacenes() {
        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .get()
            .addOnSuccessListener { documents ->
                val almacenes = documents.map { document ->
                    AlmacenData(document.id, document.getString("name") ?: "")
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item1, almacenes.map { it.name })
                almacenid = almacenes.map { it.id }.toMutableList()

                binding.formEdit2.setAdapter(adapter)
                binding.formEdit2.setDropDownBackgroundResource(android.R.color.white)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar almacenes", e)
                Toast.makeText(requireContext(), "Error al cargar almacenes.", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveProduct() {
        val name = binding.formEdit1.text.toString()
        val price = binding.formEdit3.text.toString()
        val stock = binding.formEdit4.text.toString()

        val nuevoProducto = ProductData(null, name, stock, price)

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
                                Toast.makeText(requireContext(), "Producto registrado correctamente.", Toast.LENGTH_LONG).show()
                                this@ProductForm.dismiss()
                            }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al agregar producto", e)
                Toast.makeText(requireContext(), "Error al registrar producto.", Toast.LENGTH_LONG).show()
            }
    }
}
