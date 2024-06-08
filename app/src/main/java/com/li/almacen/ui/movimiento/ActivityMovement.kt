package com.li.almacen.ui.movimiento

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.adapter.MovementAdapter
import com.li.almacen.data.MovementData
import com.li.almacen.databinding.ActivityMovementBinding
import java.util.Date

class ActivityMovement : AppCompatActivity() {
    private lateinit var adaptador: MovementAdapter
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    private lateinit var binding: ActivityMovementBinding
    private var movementList: MutableList<MovementData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el binding aquí
        binding = ActivityMovementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el RecyclerView
        val decorator = DividerItemDecoration(this@ActivityMovement, DividerItemDecoration.VERTICAL)
        adaptador = MovementAdapter(movementList)
        binding.recyclerView.adapter = adaptador
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(decorator)

        recyclerViewItem()
    }

    private fun recyclerViewItem() {
        db.collection("usuarios").document(userEmail!!).collection("movimientos")
            .get()
            .addOnSuccessListener { resultados ->
                val lista = resultados.map { document ->
                    MovementData(
                        document.id,
                        document.getString("name") ?: "",
                        document.getString("manager") ?: "",
                        document.getString("movementType") ?: "",
                        document.getDate("fecha") ?: Date()
                    )
                }
                movementList.addAll(lista)
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener datos:", e)
            }
    }
}
