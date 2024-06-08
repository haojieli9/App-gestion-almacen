package com.li.almacen.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.li.almacen.R
import com.li.almacen.databinding.ActivityMainBinding
import com.li.almacen.ui.fragments.fragmentos.FirstFragment
import com.li.almacen.ui.fragments.fragmentos.SecondFragment
import com.li.almacen.ui.fragments.fragmentos.ThirdFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
    }
}