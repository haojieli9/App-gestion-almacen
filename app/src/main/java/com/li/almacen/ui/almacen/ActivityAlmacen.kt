package com.li.almacen.ui.almacen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.adapter.CustomAdapter
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.ui.fragments.bottomsheetdialog.BottomSheetFragment
import com.li.almacen.ui.almacen.details.DetailStock
import com.li.almacen.ui.productos.ProductViewModel

class ActivityAlmacen : AppCompatActivity(), SearchView.OnQueryTextListener{
    private lateinit var binding: ActivityAlmacenBinding
    private lateinit var adaptador : CustomAdapter
    private val almacenViewModel: AlmacenViewModel by viewModels()
    private val productViewModel : ProductViewModel by viewModels()


    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()
    private var almacenList: MutableList<AlmacenData> = mutableListOf()
    private val bottomSheetFragment = BottomSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floating1.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

        //inicio toolbar
        setSupportActionBar(binding.toolbar)
        recyclerViewItem()
        recyclerViewItem2()
        initCardView()
        swipe()

        binding.swipe.setOnRefreshListener {
            recyclerViewItem()
            binding.swipe.isRefreshing = false
        }

        //inicio recyclerview
        val decorator = DividerItemDecoration(this@ActivityAlmacen, DividerItemDecoration.VERTICAL)
        adaptador = CustomAdapter(almacenList)
        binding.rvAlma.adapter = adaptador
        binding.rvAlma.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAlma.addItemDecoration(decorator)

        almacenViewModel.almacenList.observe(this, Observer { almacenList ->
            adaptador.updateList(almacenList)
            binding.tvAlm.text = almacenList.size.toString()
        })

        productViewModel.productList.observe(this, Observer { productList ->
            binding.tvArt.text = productList.sumOf { it.cantidad?.toInt() ?: 0 }.toString()
            val total = productList.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }
            binding.tvValor.text = String.format("%.2f", total)
        })

        adaptador.setOnClickListener { datos: AlmacenData, _: Int ->
            val intent = Intent(this@ActivityAlmacen, DetailStock::class.java)
            intent.putExtra("id", datos.id)
            intent.putExtra("idAlmacen", datos.id)
            intent.putExtra("nameAlmacen", datos.name)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        productViewModel.productList.observe(this, Observer { productList ->
            binding.tvArt.text = productList.size.toString()
            binding.tvValor.text = productList.sumOf { it.venta?.toDouble() ?: 0.0 }.toString()
        })

        recyclerViewItem()
        recyclerViewItem2()
    }

    private fun recyclerViewItem() {
        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .get()
            .addOnSuccessListener { resultados ->
                val lista = resultados.map { document ->
                    AlmacenData(document.id, document.data["name"].toString(), document.data["notas"].toString(), document.data["gerente"].toString(), document.data["ubicacion"].toString(), document.data["uri"])
                }.toMutableList()
                almacenViewModel.setAlmacenList(lista)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener datos:", e)
            }
    }

    private fun recyclerViewItem2() {
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
                    binding.tvArt.text = list.size.toString()
                    val total = list.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }
                    binding.tvValor.text = String.format("%.2f", total)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al obtener datos:", e)
                }
        } ?: run {
            Log.e("Firestore", "Error: userEmail es null")
        }
    }

    private fun initCardView() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            performSearch(it)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            performSearch(it)
        }
        return true
    }

    private fun performSearch(query: String) {
        val filteredAlmacenList = almacenList.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.notas.contains(query, ignoreCase = true) ||
                    it.gerente.contains(query, ignoreCase = true) ||
                    it.ubicacion.contains(query, ignoreCase = true)
        }

        almacenViewModel.almacenList.observe(this, Observer { almacenList ->
            adaptador.updateList(almacenList)
            adaptador.updateItem(filteredAlmacenList)

            binding.tvAlm.text = almacenList.size.toString()
        })

    }

    private fun swipe() {

    }
}