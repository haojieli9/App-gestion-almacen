package com.li.almacen.ui.productos

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.adapter.CustomArticulo
import com.li.almacen.adapter.ProductAdapter
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.ActivityDetailStockBinding
import com.li.almacen.databinding.ActivityProductosBinding
import com.li.almacen.ui.fragments.fullscreendialog.AlmacenForm
import com.li.almacen.ui.fragments.fullscreendialog.ProductForm


class ActivityProductos : AppCompatActivity() {
    private lateinit var binding : ActivityProductosBinding

    private lateinit var adaptador : ProductAdapter
    private var productList = mutableListOf<ProductData>()
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        actionsStart()

        adaptador = ProductAdapter(productViewModel, productList)
        val decorator = DividerItemDecoration(this@ActivityProductos, DividerItemDecoration.VERTICAL)
        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProduct.layoutManager = gridLayoutManager
        binding.rvProduct.adapter = adaptador
        binding.rvProduct.addItemDecoration(decorator)

        recyclerViewItem()

        productViewModel.productList.observe(this, Observer { productList ->
            adaptador.updateList(productList)
            binding.tvCantProductos.text = productList.size.toString()
            binding.tvTotalCoste.text = productList.sumOf { it.cantidad?.toInt() ?: 0 }.toString()
            binding.tvValor.text = productList.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }.toString()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemBack -> {
                finish()
                true
            }
            else -> { true }
        }
    }

    private fun actionsStart() {
        binding.floating1.setOnClickListener {
            val dialog = ProductForm()
            dialog.show(supportFragmentManager, "FORMALMACEN")
        }
    }
    private fun recyclerViewItem() {
        userEmail?.let { email ->
            db.collection("usuarios").document(email)
                .collection("productos")
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
                            document.getString("uri") ?: ""
                        )
                    }.toMutableList()
                    productViewModel.setProductList(list)
                    Log.d("Firestore", "Datos para detailstock: $list")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al obtener datos:", e)
                }
        } ?: run {
            Log.e("Firestore", "Error: userEmail es null")
        }
    }
}