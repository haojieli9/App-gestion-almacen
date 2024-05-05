package com.li.almacen.kt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.li.almacen.R
import com.li.almacen.apartamentos.ActivityAlmacen
import com.li.almacen.formularios.FormAlmacen

class BottomSheetFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.bts_btn1).setOnClickListener {
            val intent = Intent(context, FormAlmacen::class.java)
            startActivity(intent)
        }

    }
}