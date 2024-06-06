package com.li.almacen.ui.almacen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.adapter.CustomAdapter
import com.li.almacen.databinding.ActivityAlmacenBinding
import com.li.almacen.ui.fragments.bottomsheetdialog.BottomSheetFragment
import com.li.almacen.ui.almacen.details.DetailStock

class ActivityAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityAlmacenBinding
    private lateinit var adaptador : CustomAdapter
    private val almacenViewModel: AlmacenViewModel by viewModels()

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
        initCardView()
        swipe()

        binding.swipe.setOnRefreshListener {
            recyclerViewItem()
            binding.swipe.isRefreshing = false
        }

        //inicio recyclerview
        adaptador = CustomAdapter(almacenList)
        binding.rvAlma.adapter = adaptador
        binding.rvAlma.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        almacenViewModel.almacenList.observe(this, Observer { almacenList ->
            adaptador.updateList(almacenList)
            binding.tvCantAlm.text = almacenList.size.toString()
        })

        adaptador.setOnClickListener { datos: AlmacenData, _: Int ->
            val intent = Intent(this@ActivityAlmacen, DetailStock::class.java)
            intent.putExtra("id", datos.id)
            intent.putExtra("nameAlmacen", datos.name)
            startActivity(intent)
        }

        adaptador.setOptionClickListener { datos: AlmacenData, _: Int ->

        }
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

    private fun initCardView() {

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

    private fun swipe() {

    }
}