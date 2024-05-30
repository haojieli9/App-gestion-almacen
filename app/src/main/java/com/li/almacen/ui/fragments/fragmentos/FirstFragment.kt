package com.li.almacen.ui.fragments.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.li.almacen.test.ActivityPruebas
import com.li.almacen.ui.almacen.ActivityAlmacen
import com.li.almacen.databinding.FragmentFirstBinding
import com.li.almacen.kt.CustomArticulo
import com.li.almacen.data.AlmacenData
import com.li.almacen.kt.listaAlmacen
import com.li.almacen.kt.listaArticulo
import com.li.almacen.ui.almacen.ActivityProductos

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArt.adapter = CustomArticulo(listaArticulo)
        binding.rvArt.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.cardview2.setOnClickListener {
            val intent = Intent(requireContext(), ActivityAlmacen::class.java)
            startActivity(intent)
        }

        binding.cardviewArticulo.setOnClickListener {
            /*val intent = Intent(requireContext(), ActivityPruebas::class.java)
            startActivity(intent)*/
        }

        binding.imgMain2.setOnClickListener {
            /*val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Información general de almacenes disponibles")
            val alertDialog = builder.create()
            alertDialog.show()*/
            Snackbar.make(binding.root, "Información general de almacenes disponibles", Snackbar.LENGTH_SHORT).show()
        }

        val cantidadAlmacen = listaAlmacen.size
        binding.tvMainAlmDisp.text = cantidadAlmacen.toString()

        val totalArticulo = listaArticulo.size
        binding.tvMainArtTot.text = totalArticulo.toString() + " units"

        val totalValue = listaArticulo.sumOf { it.precio.toDouble() }.toFloat()
        binding.tvMainValTot.text = totalValue.toString() + "€"

        binding.cardview3.setOnClickListener() {
/*            val intent = Intent(requireContext(), ActivityPruebas::class.java)
            startActivity(intent)*/
            val intent = Intent(requireContext(), ActivityProductos::class.java)
            this@FirstFragment.startActivity(intent)
        }

        binding.cardview5.setOnClickListener {
            val intent = Intent(requireContext(), ActivityPruebas::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
