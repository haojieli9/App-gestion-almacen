package com.li.almacen

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.li.almacen.apartamentos.ActivityAlmacen
import com.li.almacen.databinding.ActivityMainBinding
import com.li.almacen.databinding.ActivityPruebasBinding
import com.li.almacen.fragmentos.FirstFragment
import com.li.almacen.fragmentos.SecondFragment
import com.li.almacen.fragmentos.ThirdFragment
import com.li.almacen.kt.CustomAdapter
import com.li.almacen.kt.CustomArticulo
import com.li.almacen.kt.listaAlmacen
import com.li.almacen.kt.listaArticulo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        /*

        binding.rvArt.adapter = CustomArticulo(listaArticulo)
        binding.rvArt.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.cardview2.setOnClickListener {
            val intent = Intent(this@MainActivity, ActivityAlmacen::class.java)
            startActivity(intent)
        }

        binding.cardviewArticulo.setOnClickListener {
            val intent = Intent(this@MainActivity, ActivityPruebas::class.java)
            startActivity(intent)
        }

        binding.imgMain2.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Información general de almacenes disponibles")
            //builder.setPositiveButton("ACEPTAR") { dialog, _ -> dialog.dismiss() }
            val alertDialog = builder.create()
            alertDialog.show()
            //Toast.makeText(this, "Información general de almacenes disponibles", Toast.LENGTH_SHORT).show()
        }

        val cantidadAlmacen = listaAlmacen.size
        binding.tvMainAlmDisp.text = cantidadAlmacen.toString()

        val totalArticulo = listaArticulo.size
        binding.tvMainArtTot.text = totalArticulo.toString() + " units"

        val totalValue = listaArticulo.sumOf { it.precio.toDouble() }.toFloat()
        binding.tvMainValTot.text = totalValue.toString() + "€"
*/
        val firstFragment = FirstFragment()
        val secondFragment = SecondFragment()
        val thirdFragment = ThirdFragment()

        setCurrentFragment(firstFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.products -> setCurrentFragment(secondFragment)
                R.id.perfil -> setCurrentFragment(thirdFragment)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
    }


}