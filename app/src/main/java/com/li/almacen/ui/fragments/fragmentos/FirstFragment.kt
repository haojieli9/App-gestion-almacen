package com.li.almacen.ui.fragments.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.li.almacen.test.ActivityPruebas
import com.li.almacen.ui.almacen.ActivityAlmacen
import com.li.almacen.databinding.FragmentFirstBinding
import com.li.almacen.ui.productos.ActivityProductos

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

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

        binding.cardviewArticulo.setOnClickListener {

        }

        binding.imgMain2.setOnClickListener {
            Snackbar.make(binding.root, "Información general de almacenes disponibles", Snackbar.LENGTH_SHORT).show()
        }

        binding.cardview3.setOnClickListener() {
            val intent = Intent(requireContext(), ActivityProductos::class.java)
            this@FirstFragment.startActivity(intent)
        }

        binding.cardview5.setOnClickListener {
            val intent = Intent(requireContext(), ActivityPruebas::class.java)
            startActivity(intent)
        }

        // init detail data

    }
}
