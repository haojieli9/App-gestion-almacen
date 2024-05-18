package com.li.almacen.ui.almacen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.kt.CustomAdapter
import com.li.almacen.kt.listaAlmacen
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.ui.fragments.bottomsheetdialog.BottomSheetFragment
import com.li.almacen.kt.listaArticulo

class ActivityAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityAlmacenBinding
    private var almacenList: MutableList<AlmacenData> = mutableListOf()
    private lateinit var adaptador : CustomAdapter

    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetFragment = BottomSheetFragment()

        binding.floating1.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDiaglog")
        }

        //inicio toolbar
        setSupportActionBar(binding.toolbar)
        initCardView()
        recyclerViewItem()

        //inicio recyclerview
        adaptador = CustomAdapter(almacenList)
        binding.rvAlma.adapter = adaptador
        binding.rvAlma.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adaptador.setOnClickListener { _: AlmacenData, _: Int ->
            Toast.makeText(this@ActivityAlmacen, "Clicked", Toast.LENGTH_SHORT).show()
        }

    }

    private fun recyclerViewItem() {
        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .get()
            .addOnSuccessListener { resultados ->
            for (document in resultados) {
                Log.d("Datos documentos: ",  "${document.id} ${document.data}")
                val almacen = document.toObject(AlmacenData::class.java)
                val lista: MutableList<AlmacenData> = mutableListOf(
                    AlmacenData(document.id, document.data["Nombre almacen"].toString(), document.data["Descripcion"].toString(), document.data["Encargado"].toString(), document.data["Capacidad"].toString(), document.data["Ubicacion"].toString()),
                )
                Log.d("Lista: ", lista.toString())
                almacenList.addAll(lista)

                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener datos:", e)
            }
    }

    private fun initCardView() {
        //cardview informacion general
        val cantidadAlmacen = listaAlmacen.size
        binding.tvCantAlm.text = cantidadAlmacen.toString()

        val totalArticulo = listaArticulo.size
        binding.tvUnitArt.text = totalArticulo.toString() + " units"

        val totalValue = listaArticulo.sumOf { it.precio.toDouble() }.toFloat()
        binding.tvValor.text = totalValue.toString() + "€"

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
}