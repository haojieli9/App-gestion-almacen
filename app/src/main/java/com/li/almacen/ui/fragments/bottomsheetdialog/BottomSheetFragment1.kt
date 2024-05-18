package com.li.almacen.ui.fragments.bottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.li.almacen.databinding.BottomsheetAlmacenBinding

open class BottomSheetFragment1: BottomSheetDialogFragment() {

    private var _binding: BottomsheetAlmacenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetAlmacenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btsLayout

        binding.btsBtn1.setOnClickListener {
            Toast.makeText(requireContext(), "Hello world", Toast.LENGTH_SHORT).show()
        }

        binding.btsBtn2.setOnClickListener {
            Toast.makeText(requireContext(), "Funcionalidad no disponible", Toast.LENGTH_SHORT).show()
        }
    }
}