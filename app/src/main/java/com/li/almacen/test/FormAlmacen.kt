package com.li.almacen.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.li.almacen.databinding.ActivityFormAlmacenBinding

class FormAlmacen : AppCompatActivity() {
    private lateinit var binding: ActivityFormAlmacenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAlmacenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val code = intent.getIntExtra("scanResult", 0)
        binding.naEdit1.setText(code.toString())

        /*binding.btGuardar.setOnClickListener {
            val tfNombre = binding.tfNombre
            val tfDir = binding.tfDir

            if (binding.tfDir.text.toString().isEmpty() || binding.tfNombre.text.toString().isEmpty()) {
                Toast.makeText(this@FormAlmacen, "Los campos no puede estar vacio.", Toast.LENGTH_SHORT).show()
            } else {
                listaAlmacen.add(Almacenes(tfNombre.text.toString(), tfDir.text.toString(), "https://loremflickr.com/80/80?lock=1"))
                Toast.makeText(this@FormAlmacen, "Almacén creado.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FormAlmacen, ActivityAlmacen::class.java)
                startActivity(intent)
                this@FormAlmacen.finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.third_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemIntentBack -> {
                val intent = Intent(this, ActivityAlmacen::class.java)
                startActivity(intent)
                this@FormAlmacen.finish()
                true
            } else -> { true }
        }*/
    }
}