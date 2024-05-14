package com.li.almacen.ui.fragments.fragmentos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.li.almacen.ui.login.Login
import com.li.almacen.test.Politica_web
import com.li.almacen.R


class ThirdFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        val cardv4 = view.findViewById<CardView>(R.id.per_card4)

        // OnClickListener para el botón
        cardv4.setOnClickListener {
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
            this.requireActivity().finish()
        }

        val about = view.findViewById<MaterialButton>(R.id.about)

        about.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://tameformacion.com/"))
            startActivity(intent)
        }

        val politica = view.findViewById<MaterialButton>(R.id.per_politica)

        politica.setOnClickListener {
            val intent2 = Intent(requireContext(), Politica_web::class.java)
            startActivity(intent2)
        }

        return view
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}