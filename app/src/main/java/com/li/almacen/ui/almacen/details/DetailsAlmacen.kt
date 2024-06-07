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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.databinding.ActivityDetailsAlmacenBinding
import com.li.almacen.ui.almacen.AlmacenViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class DetailsAlmacen : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsAlmacenBinding
    private var toolbar: Toolbar? = null
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private val almacenViewModel: AlmacenViewModel by viewModels()
    var uri : Uri? = null
    var newUri : Uri? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { ur ->
        if (ur != null) {
            binding.detailImage.setImageURI(ur)
            uri = ur
            newUri = ur
            Log.d("URI", uri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initData()
        componentValidation()

    }

    private fun componentValidation() {
        // Component validation
        validateEditText(binding.formTilId, binding.formEditId)
        validateEditText(binding.formTil1, binding.formEdit1)
        validateEditText(binding.formTil2, binding.formEdit2)
        validateEditText(binding.formTil3, binding.formEdit3)
        validateEditText(binding.formTil5, binding.formEdit5)
        validateEditText(binding.formTilDate, binding.formEditDate)

        binding.detailImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initData() {
        val id = intent.getStringExtra("id") ?: ""
        val nombre = intent.getStringExtra("name") ?: ""
        val descripcion = intent.getStringExtra("description") ?: ""
        val encargado = intent.getStringExtra("encargado") ?: ""
        val detailsUbi = intent.getStringExtra("ubicacion") ?: ""
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
                val formEditIdText = binding.formEditId.text.toString()
                val formEdit1Text = binding.formEdit1.text.toString()
                val formEdit2Text = binding.formEdit2.text.toString()
                val formEdit3Text = binding.formEdit3.text.toString()
                val formEdit5Text = binding.formEdit5.text.toString()
                val formEditDateText = binding.formEditDate.text.toString()

                when {
                    formEditIdText.isEmpty() -> {
                        binding.formTilId.error = "Este campo es obligatorio."
                        Toast.makeText(this,"El ID es obligatorio.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit1Text.isEmpty() -> {
                        binding.formTil1.error = "Este campo es obligatorio."
                        Toast.makeText(this,"El nombre es obligatorio.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit2Text.isEmpty() -> {
                        binding.formTil2.error = "Este campo es obligatorio."
                        Toast.makeText(this,"La descripción es obligatoria.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit3Text.isEmpty() -> {
                        binding.formTil3.error = "Este campo es obligatorio."
                        Toast.makeText(this,"El encargado es obligatorio.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit5Text.isEmpty() -> {
                        binding.formTil5.error = "Este campo es obligatorio."
                        Toast.makeText(this,"La ubicación es obligatoria.", Toast.LENGTH_SHORT).show()
                    }
                    formEditDateText.isEmpty() -> {
                        binding.formTilDate.error = "Este campo es obligatorio."
                        Toast.makeText(this,"La fecha es obligatoria.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        this@DetailsAlmacen.finish()
                        dialog.dismiss()
                        guardarCambios()
                    }
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun guardarCambios() {
        val id = intent.getStringExtra("id")
        val nombre = binding.formEdit1.text.toString()
        val descripcion = binding.formEdit2.text.toString()
        val empleado = binding.formEdit3.text.toString()
        val fecha = intent.getStringExtra("fecha")
        val ubicacion = binding.formEdit5.text.toString()

        // Llamar a la función updateAlmacen del ViewModel pasando los nuevos datos
        updateAlmacen(id, nombre, descripcion, empleado, fecha!!, ubicacion)
    }


    private fun updateAlmacen(id: String?, nombre: String, descripcion: String, empleado: String, fecha: String, ubicacion: String) {
        // Verifica si el ID no es nulo
        id?.let { almacenId ->
            // Crea un nuevo objeto AlmacenData con los datos actualizados
            val almacenActualizado = AlmacenData(
                id,
                nombre,
                descripcion,
                empleado,
                ubicacion,
                newUri,
                Date.from(
                    LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
                )
            )

            // Actualiza los datos en Firestore
            db.collection("usuarios").document(userEmail!!).collection("almacenes")
                .document(almacenId)
                .set(almacenActualizado)
                .addOnSuccessListener {
                    almacenViewModel.updateAlmacen(almacenActualizado) // Suponiendo que tengas un método para actualizar en el ViewModel
                    Toast.makeText(
                        this@DetailsAlmacen,
                        "Almacén actualizado correctamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al actualizar almacén - AlmacenForm", e)
                    Toast.makeText(
                        this@DetailsAlmacen,
                        "Error al actualizar almacén.",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}