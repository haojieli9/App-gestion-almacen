package com.li.almacen.ui.fragments.fullscreendialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.databinding.CustomDialogBinding


class ExampleDialog : DialogFragment() {
    private var db = FirebaseFirestore.getInstance()
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email

    private lateinit var binding: CustomDialogBinding
    private var toolbar: Toolbar? = null
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
        binding = CustomDialogBinding.inflate(inflater, container, false)
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
                    val nombre = binding.formEdit1.editText?.text.toString()
                    val descripcion = binding.formEdit3.editText?.text.toString()
                    val empleado = binding.formEdit5.editText?.text.toString()
                    val capacidad = binding.formEdit6.editText?.text.toString()
                    val ubicacion = binding.formEdit2.editText?.text.toString()

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
                    true
                }
                else -> {
                    dismiss()
                    false
                }
            }
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

}