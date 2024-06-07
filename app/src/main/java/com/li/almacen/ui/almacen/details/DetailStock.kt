package com.li.almacen.ui.almacen.details

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.ActivityDetailStockBinding
import com.li.almacen.adapter.CustomArticulo
import com.li.almacen.ui.fragments.fullscreendialog.ProductForm
import com.li.almacen.ui.productos.ProductViewModel

open class DetailStock : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStockBinding
    private lateinit var adaptador : CustomArticulo
    private var productList = mutableListOf<ProductData>()
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initData
        recyclerViewItem()
        actionsStart()
        swipe()
        binding.imgEmpty.isVisible = false

        binding.swipe.setOnRefreshListener {
            recyclerViewItem()
            binding.swipe.isRefreshing = false
        }
    }

    private fun recyclerViewItem() {
        val almacenId = intent.getStringExtra("id")

        if (almacenId == null) {
            Log.e("Firestore", "Error: almacenId is null DetailStock - 37")
            return
        }

        db.collection("usuarios").document(userEmail!!)
            .collection("productos_almacenes")
            .whereEqualTo("almacenId", almacenId)
            .get()
            .addOnSuccessListener { relationResults ->
                if (relationResults.isEmpty) {
                    Log.e("Firestore", "No documents found in productos_almacenes for almacenId: $almacenId")
                    binding.imgEmpty.isVisible = true
                } else {
                    Log.d("Firestore", "Documents found: ${relationResults.documents.size}")
                    binding.imgEmpty.isVisible = false
                }

                val productIds = relationResults.mapNotNull { document ->
                    val productId = document.data["productoId"]
                    Log.d("Firestore product almacen: ", "Document data: ${document.id } ${document.data["productoId"] } DetailStock - 56")
                    productId
                }

                if (productIds.isEmpty()) {
                    Log.e("Firestore", "No productIds found for almacenId: $almacenId DetailStock - 59")
                    return@addOnSuccessListener
                }

                Log.d("Firestore", "Product IDs obtained: $productIds")

                db.collection("usuarios").document(userEmail!!)
                    .collection("productos")
                    .whereIn(FieldPath.documentId(), productIds)
                    .get()
                    .addOnSuccessListener { productResults ->
                        val list = productResults.map { document ->
                            ProductData(
                                document.id,
                                document.getString("name") ?: "",
                                document.getString("barcode") ?: "",
                                document.getString("almacenDestino") ?: "",
                                document.getString("categoria") ?: "",
                                document.getString("proveedor") ?: "",
                                document.getString("cantidad") ?: "",
                                document.getString("coste") ?: "",
                                document.getString("venta") ?: "",
                                document.getString("descriptor") ?: "",
                                document.getString("fechaVencimiento") ?: "",
                                document.getString("uri") ?: "")
                        }.toMutableList()
                        productList.addAll(list)
                        Log.d("Firestore", "Datos para detailstock: $productList")
                        adaptador = CustomArticulo(productList)
                        val decorator = DividerItemDecoration(this@DetailStock, DividerItemDecoration.VERTICAL)
                        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerView.layoutManager = gridLayoutManager
                        binding.recyclerView.adapter = adaptador
                        binding.recyclerView.addItemDecoration(decorator)

                        binding.tvCantProductos.text = productList.size.toString()
                        binding.tvTotalCoste.text = productList.sumOf { it.cantidad?.toInt() ?: 0 }.toString()
                        binding.tvValor.text = productList.sumOf { it.venta?.toDouble() ?: 0.0 }.toString()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al obtener datos:", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener relaciones productos_almacenes:", e)
            }
    }

    private fun swipe() {}

    private fun actionsStart() {
        binding.floatingButton.setOnClickListener {
            val nameAlmacen = intent.getStringExtra("nameAlmacen")
            val idAlmacen = intent.getStringExtra("idAlmacen")
            if (nameAlmacen != null) {
                ProductForm.display(supportFragmentManager, nameAlmacen, idAlmacen)
            }
        }
    }

}