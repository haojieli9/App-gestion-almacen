package com.li.almacen.test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.li.almacen.ui.fragments.fullscreendialog.AlmacenForm
import com.li.almacen.databinding.ActivityPruebasBinding


class ActivityPruebas : AppCompatActivity() {
    lateinit var binding: ActivityPruebasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPruebasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testButton1.setOnClickListener { v -> openDialog() }

        binding.testButton2.setOnClickListener {
            initScanner()
        }

    }



    private fun openDialog() {
        AlmacenForm.display(supportFragmentManager)

    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
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
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
                val intent = Intent(this, FormAlmacen::class.java)
                intent.putExtra("scanResult", result.contents)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}