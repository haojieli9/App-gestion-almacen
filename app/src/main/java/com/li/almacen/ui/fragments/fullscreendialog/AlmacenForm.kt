package com.li.almacen.ui.fragments.fullscreendialog

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
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
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class AlmacenForm : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var toolbar: Toolbar? = null
    private val almacenViewModel: AlmacenViewModel by activityViewModels()
    var uri: Uri? = null
    private lateinit var binding: FormAlmacenBinding

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { ur ->
        if (ur != null) {
            binding.imgPicker.setImageURI(ur)
            uri = ur
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
                    this@AlmacenForm.dismiss()
                    false
                }
            }
        }

        // Component validation
        validateEditText(binding.formTil1, binding.formEdit1)
        validateEditText(binding.formTil2, binding.formEdit2)
        validateEditText(binding.formTil3, binding.formEdit3)
        validateEditText(binding.formTil5, binding.formEdit5)

        binding.imgPicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    companion object {
        private const val TAG = "FORMALMACEN"
        fun display(fragmentManager: FragmentManager?): AlmacenForm {
            val almacenForm = AlmacenForm()
            almacenForm.show(fragmentManager!!, TAG)
            return almacenForm
        }
    }

    private fun stockRegister() {
        // Verificar si el fragmento está añadido antes de proceder
        if (!isAdded) return

        val nombre = binding.formEdit1.text.toString()
        val descripcion = binding.formEdit2.text.toString()
        val empleado = binding.formEdit3.text.toString()
        val ubicacion = binding.formEdit5.text.toString()

        val nuevoAlmacen = AlmacenData(
            null, nombre, descripcion, empleado, ubicacion, uri,
            Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        )

        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .add(nuevoAlmacen)
            .addOnSuccessListener { documentReference ->
                nuevoAlmacen.id = documentReference.id

                documentReference.update("id", nuevoAlmacen.id)
                    .addOnSuccessListener {
                        if (isAdded) {
                            almacenViewModel.addAlmacen(nuevoAlmacen)
                            Toast.makeText(requireContext(), "Almacén registrado correctamente.", Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al agregar almacén - AlmacenForm 137", e)
                if (isAdded) {
                    Toast.makeText(requireContext(), "Error al registrar almacén.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun confirmDialogBuilder() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Deseas guardar este almacén?")
            .setTitle("Guardar cambios")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Confirmar") { dialog, _ ->

                val formEdit1Text = binding.formEdit1.text.toString()
                val formEdit2Text = binding.formEdit2.text.toString()
                val formEdit3Text = binding.formEdit3.text.toString()
                val formEdit5Text = binding.formEdit5.text.toString()
                when {
                    formEdit1Text.isEmpty() -> {
                        binding.formTil1.error = "Este campo es obligatorio."
                        Toast.makeText(requireContext(), "El nombre es obligatorio.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit2Text.isEmpty() -> {
                        binding.formTil2.error = "Este campo es obligatorio."
                        Toast.makeText(requireContext(), "La descripción es obligatoria.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit3Text.isEmpty() -> {
                        binding.formTil3.error = "Este campo es obligatorio."
                        Toast.makeText(requireContext(), "El encargado es obligatorio.", Toast.LENGTH_SHORT).show()
                    }
                    formEdit5Text.isEmpty() -> {
                        binding.formTil5.error = "Este campo es obligatorio."
                        Toast.makeText(requireContext(), "La ubicación es obligatoria.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        stockRegister()
                        dialog.dismiss()
                        this@AlmacenForm.dismiss()
                    }
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
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
