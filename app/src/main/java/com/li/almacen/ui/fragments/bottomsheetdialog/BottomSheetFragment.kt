package com.li.almacen.ui.fragments.bottomsheetdialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.li.almacen.databinding.BottomsheetFragmentBinding
import com.li.almacen.test.FormAlmacen
import com.li.almacen.ui.fragments.fullscreendialog.AlmacenForm
import com.li.almacen.ui.fragments.fullscreendialog.ProductForm
import com.li.almacen.ui.productos.ProductViewModel
import com.li.almacen.ui.productos.details.DetailProduct

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetFragmentBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btsBtn1.setOnClickListener { openDialog() }

        binding.btnScanner.setOnClickListener { initScanner() }

        binding.btsBtn2.setOnClickListener { openDialogProduct() }
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

    private fun openDialog() {
        val dialog = AlmacenForm()
        dialog.show(childFragmentManager, "FORMALMACEN")
    }

    private fun openDialogProduct() {
        val dialog = ProductForm()
        dialog.show(childFragmentManager, "FORMPRODUCTO")
    }
}
