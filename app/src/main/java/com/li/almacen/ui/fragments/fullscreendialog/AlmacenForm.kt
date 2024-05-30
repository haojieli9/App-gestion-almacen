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
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.databinding.FormAlmacenBinding
import com.li.almacen.ui.almacen.AlmacenViewModel

class ExampleDialog : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var toolbar: Toolbar? = null
    private val almacenViewModel: AlmacenViewModel by activityViewModels()
    private lateinit var binding: FormAlmacenBinding

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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
        binding = FormAlmacenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = binding.toolbar

        toolbar?.setNavigationOnClickListener { dismiss() }
        toolbar?.title = "Nuevo almacen"
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
        validateEditText(binding.formTil2, binding.formEdit2)
        validateEditText(binding.formTil3, binding.formEdit3)
        validateEditText(binding.formTil4, binding.formEdit4)
        validateEditText(binding.formTil5, binding.formEdit5)

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
        if (!isAdded) return

        val nombre = binding.formEdit1.text.toString()
        val descripcion = binding.formEdit2.text.toString()
        val empleado = binding.formEdit3.text.toString()
        val capacidad = binding.formEdit4.text.toString()
        val ubicacion = binding.formEdit5.text.toString()

        val nuevoAlmacen = AlmacenData(null, nombre, descripcion, empleado, capacidad, ubicacion)

        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .add(nuevoAlmacen)
            .addOnSuccessListener { documentReference ->
                nuevoAlmacen.id = documentReference.id

                documentReference.update("id", nuevoAlmacen.id)
                    .addOnSuccessListener {
                        almacenViewModel.addAlmacen(nuevoAlmacen)
                        Toast.makeText(requireContext(), "Almacén registrado correctamente.", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al agregar almacén", e)
                Toast.makeText(requireContext(), "Error al registrar almacén.", Toast.LENGTH_LONG).show()
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
            stockRegister()
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
}
