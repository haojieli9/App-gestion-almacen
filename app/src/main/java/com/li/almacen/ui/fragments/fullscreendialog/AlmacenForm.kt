package com.li.almacen.ui.fragments.fullscreendialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.databinding.FormAlmacenBinding
import com.li.almacen.ui.almacen.AlmacenViewModel


class ExampleDialog : DialogFragment() {
    private var db = FirebaseFirestore.getInstance()
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var toolbar: Toolbar? = null

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri!=null) {
            binding.imgPicker.setImageURI(uri)
            Log.d("URI", uri.toString())
        } else {
            // no hay imagen
        }
    }

    private lateinit var almacenViewModel: AlmacenViewModel
    private lateinit var binding: FormAlmacenBinding

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
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
        binding = FormAlmacenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = binding.toolbar

        super.onViewCreated(view, savedInstanceState)
        toolbar!!.setNavigationOnClickListener { _: View? -> dismiss() }
        toolbar!!.setTitle("Nuevo almacen")
        toolbar!!.inflateMenu(R.menu.example_dialog)
        toolbar!!.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSave -> {
                    confirmDialogBuilder()
                    //stockRegister()
                    true
                }
                else -> {
                    dismiss()
                    false
                }
            }
        }

        // Component validation
        validateEditText(R.id.formTil1, R.id.formEdit1)
        validateEditText(R.id.formTil2, R.id.formEdit2)
        validateEditText(R.id.formTil3, R.id.formEdit3)
        validateEditText(R.id.formTil4, R.id.formEdit4)
        validateEditText(R.id.formTil5, R.id.formEdit5)

        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    companion object {
        private const val TAG = "FORMALMACEN"
        fun display(fragmentManager: FragmentManager?): ExampleDialog {
            val exampleDialog = ExampleDialog()
            exampleDialog.show(fragmentManager!!, TAG)
            return exampleDialog
        }
    }

    private fun stockRegister() {

        val nombre = binding.formEdit1.text.toString()
        val descripcion = binding.formEdit2.text.toString()
        val empleado = binding.formEdit3.text.toString()
        val capacidad = binding.formEdit4.text.toString()
        val ubicacion = binding.formEdit5.text.toString()

        val nuevoAlmacen = hashMapOf(
            "Nombre almacen" to nombre,
            "Descripcion" to descripcion,
            "Encargado" to empleado,
            "Capacidad" to capacidad,
            "Ubicacion" to ubicacion
        )

        // Obtenemos la referencia a la colección de almacenes del usuario actual
        val usuarioAlmacenesRef = db.collection("usuarios").document(userEmail!!).collection("almacenes")

        // Añadimos el nuevo almacén a la colección del usuario
        usuarioAlmacenesRef
            .add(nuevoAlmacen)
            .addOnSuccessListener { documentReference ->
                // Éxito al agregar el nuevo almacén
                val nuevoId = documentReference.id
                println("Almacén agregado con ID: $nuevoId")
            }
            .addOnFailureListener { e ->
                // Error al agregar el nuevo almacén
                println("Error al agregar el almacén: $e")
            }

        Toast.makeText(requireContext(), nombre, Toast.LENGTH_SHORT).show()
        dismiss()
    }

    private fun confirmDialogBuilder() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.confirm_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(getDrawable(resources, R.color.transparent, null))
        dialog.setCancelable(false)
        dialog.show()

        val cancel = dialog.findViewById<Button>(R.id.testButton1)
        val yes = dialog.findViewById<Button>(R.id.testButton2)

        cancel.setOnClickListener {
            Toast.makeText(requireContext(), "Operación cancelada.", Toast.LENGTH_LONG).show()
            dialog.dismiss()
            this@ExampleDialog.dismiss()
        }

        yes.setOnClickListener {
            stockRegister()
            Toast.makeText(requireContext(), "Almacen registrado correctamente.", Toast.LENGTH_LONG).show()
            dialog.dismiss()
            this@ExampleDialog.dismiss()
        }
    }

    private fun validateEditText(layout: Int, edit: Int) {
        val lay = requireView().findViewById<TextInputLayout>(layout)
        val editt = requireView().findViewById<TextInputEditText>(edit)

        editt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                when {
                    text.isEmpty() -> lay.error = "Este campo es obligatorio."
//                    text.length < 8 -> lay.error = "El nombre debe tener al menos 8 caracteres."
                    else -> lay.error = null
                }
            }
        })
    }

    private fun mediaPicker() {

    }
}