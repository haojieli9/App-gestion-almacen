package com.li.almacen.ui.fragments.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.li.almacen.R
import com.li.almacen.adapter.CustomAdapter
import com.li.almacen.adapter.ProductAdapter
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.ProductData
import com.li.almacen.test.ActivityPruebas
import com.li.almacen.ui.almacen.ActivityAlmacen
import com.li.almacen.databinding.FragmentFirstBinding
import com.li.almacen.ui.fragments.bottomsheetdialog.BottomSheetFragment
import com.li.almacen.ui.productos.ActivityProductos
import com.li.almacen.ui.productos.ProductViewModel
import com.li.almacen.ui.productos.details.DetailProduct

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()
    private val bottomSheetFragment = BottomSheetFragment()
    private var barcode:String = ""
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var adaptador2: ProductAdapter
    private var productList = mutableListOf<ProductData>()

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConfig()
        recyclerViewItem()
        recyclerViewItem2()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        binding.floating1.setOnClickListener {
            bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
        }

        adaptador2 = ProductAdapter(productViewModel, productList)
        val decotar = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val gridLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvArt.layoutManager = gridLayoutManager
        binding.rvArt.adapter = adaptador2
        binding.rvArt.addItemDecoration(decotar)

        productViewModel.productList.observe(viewLifecycleOwner, Observer { productList ->
            adaptador2.updateList(productList)
            binding.tvMainArtTot.text = productList.sumOf { it.cantidad?.toInt() ?: 0 }.toString()
            binding.tvMainValTot.text = productList.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }.toString()
        })
    }

    override fun onResume() {
        super.onResume()
        recyclerViewItem()
        recyclerViewItem2()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initConfig() {
        // init onClickListener()
        binding.cardview2.setOnClickListener {
            val intent = Intent(requireContext(), ActivityAlmacen::class.java)
            startActivity(intent)
        }

        binding.imgMain2.setOnClickListener {
            Snackbar.make(binding.root, "Información general de almacenes disponibles", Snackbar.LENGTH_SHORT).show()
        }

        binding.cardview3.setOnClickListener {
            val intent = Intent(requireContext(), ActivityProductos::class.java)
            this@FirstFragment.startActivity(intent)
        }
    }

    private fun recyclerViewItem() {
        db.collection("usuarios").document(userEmail!!).collection("almacenes")
            .get()
            .addOnSuccessListener { resultados ->
                val lista = resultados.map { document ->
                    AlmacenData(document.id, document.data["name"].toString(), document.data["notas"].toString(), document.data["gerente"].toString(), document.data["ubicacion"].toString(), document.data["uri"])
                }.toMutableList()
                binding.tvMainAlmDisp.text = lista.size.toString()

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
                    productViewModel.setProductList(list)
                    binding.tvMainArtTot.text = list.size.toString()
                    binding.tvMainValTot.text = list.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }.toString()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al obtener datos:", e)
                }
        } ?: run {
            Log.e("Firestore", "Error: userEmail es null")
        }
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        inflater.inflate(R.menu.scanner_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.itemScanner -> {
                initScanner()
                true
            }
            else -> { true }
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("BARCODE SCANNER")
        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Operación cancelada", Toast.LENGTH_LONG).show()
            } else {
                onBarcodeScanned(result.contents)
                Toast.makeText(requireContext(), "Codigo barra: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun onBarcodeScanned(barcode: String) {
        val product = productViewModel.isRepeatBarcode(barcode)
        if (product != null) {
            val intent = Intent(context, DetailProduct::class.java).apply {
                putExtra("idProd", product.id)
                putExtra("nameProd", product.name)
                putExtra("barcodeProd", product.barcode)
                putExtra("almacenProd", product.almacenDestino)
                putExtra("categoriaProd", product.categoria)
                putExtra("proveedorProd", product.proveedor)
                putExtra("cantidadProd", product.cantidad)
                putExtra("costeProd", product.coste)
                putExtra("ventaProd", product.venta)
                putExtra("descripProd", product.descriptor)
                putExtra("fechaVencimientoProd", product.fechaVencimiento)
                putExtra("uriProd", product.uri.toString())
            }
            startActivity(intent)
        } else {
            Toast.makeText(context, "Este producto no esta registrado.", Toast.LENGTH_SHORT).show()
        }
    }

}

