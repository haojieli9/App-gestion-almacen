package com.li.almacen.ui.fragments.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.adapter.CustomAdapter
import com.li.almacen.adapter.ProductAdapter
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.ProductData
import com.li.almacen.databinding.FragmentSecondBinding
import com.li.almacen.ui.almacen.AlmacenViewModel
import com.li.almacen.ui.productos.ProductViewModel

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var adaptador: CustomAdapter
    private lateinit var adaptador2: ProductAdapter

    private var almacenList: MutableList<AlmacenData> = mutableListOf()
    private var productList = mutableListOf<ProductData>()

    private val almacenViewModel: AlmacenViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()

    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        recyclerViewItem()

        val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        adaptador = CustomAdapter(almacenList)
        binding.rvAlma.adapter = adaptador
        binding.rvAlma.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAlma.addItemDecoration(decorator)

        adaptador2 = ProductAdapter(productViewModel, productList)
        val decotar = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val gridLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvProduct.layoutManager = gridLayoutManager
        binding.rvProduct.adapter = adaptador2
        binding.rvProduct.addItemDecoration(decotar)
        recyclerViewItem2()

        binding.swipe.setOnRefreshListener {
            recyclerViewItem()
            recyclerViewItem2()
            binding.swipe.isRefreshing = false
            binding.swipe2.isRefreshing = false
        }

        almacenViewModel.almacenList.observe(viewLifecycleOwner, Observer { almacenList ->
            adaptador.updateList(almacenList)
        })

        productViewModel.productList.observe(viewLifecycleOwner, Observer { productList ->
            adaptador2.updateList(productList)
            binding.tvMainArtTot.text = productList.sumOf { it.cantidad?.toInt() ?: 0 }.toString()
            binding.tvMainValTot.text = productList.sumOf { it.venta?.toDouble()!! * it.cantidad!!.toInt() }.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
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
