package com.li.almacen.ui.almacen.details

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.ActivityDetailStockBinding
import com.li.almacen.adapter.CustomArticulo

open class DetailStock : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStockBinding
    private lateinit var adaptador : CustomArticulo

    open var productList: MutableList<ProductData> = mutableListOf()
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initData
        recyclerViewItem()

    }

    private fun recyclerViewItem() {
        val almacenId = intent.getStringExtra("id")

        if (almacenId == null) {
            Log.e("Firestore", "Error: almacenId is null DetailStock - 39")
            return
        }

        db.collection("usuarios").document(userEmail!!)
            .collection("productos_almacenes")
            .whereEqualTo("almacenId", almacenId)
            .get()
            .addOnSuccessListener { relationResults ->
                if (relationResults.isEmpty) {
                    Log.e("Firestore", "No documents found in productos_almacenes for almacenId: $almacenId")
                } else {
                    Log.d("Firestore", "Documents found: ${relationResults.documents.size}")
                }

                val productIds = relationResults.mapNotNull { document ->
                    val productId = document.data["productoId"]
                    Log.d("Firestore product almacen: ", "Document data: ${document.id } ${document.data["productoId"] } DetailStock - 56")
                    productId
                }

                if (productIds.isEmpty()) {
                    Log.e("Firestore", "No productIds found for almacenId: $almacenId DetailStock - 61")
                    return@addOnSuccessListener
                }

                Log.d("Firestore", "Product IDs obtained: $productIds")

                db.collection("usuarios").document(userEmail!!)
                    .collection("productos")
                    .whereIn(FieldPath.documentId(), productIds)
                    .get()
                    .addOnSuccessListener { productResults ->
                        val productList = productResults.map { document ->
                            ProductData(
                                document.id,
                                document.getString("cantidad") ?: "",
                                document.getString("name") ?: "",
                                document.getString("price") ?: ""
                            )
                        }.toMutableList()

                        Log.d("Firestore", "Datos: $productList")
                        adaptador = CustomArticulo(productList)
                        val gridLayoutManager = GridLayoutManager(this, 3)
                        binding.recyclerView.layoutManager = gridLayoutManager
                        binding.recyclerView.adapter = adaptador
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al obtener datos:", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener relaciones productos_almacenes:", e)
            }
    }
}