package com.li.almacen.ui.fragments.bottomsheetdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.li.almacen.databinding.BottomsheetFragmentBinding
import com.li.almacen.test.FormAlmacen
import com.li.almacen.ui.fragments.fullscreendialog.ExampleDialog

open class BottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: BottomsheetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btsLayout

        binding.btsBtn1.setOnClickListener { v -> openDialog()}

        binding.btnScanner.setOnClickListener { initScanner() }

        binding.btsBtn2.setOnClickListener {
            Toast.makeText(requireContext(), "Funcionalidad no disponible", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
                val intent = Intent(requireContext(), FormAlmacen::class.java)
                intent.putExtra("scanResult", result.contents)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openDialog() {
        val dialog = ExampleDialog()
        dialog.show(childFragmentManager, "FORMALMACEN")
    }
}