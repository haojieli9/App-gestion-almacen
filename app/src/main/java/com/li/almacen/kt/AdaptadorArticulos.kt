package com.li.almacen.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.li.almacen.R
import com.li.almacen.data.ProductData

class CustomArticulo(private var listaArticulos: MutableList<ProductData>) : RecyclerView.Adapter<CustomArticulo.ViewHolderArticulo>() {

    class ViewHolderArticulo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArID: TextView
        val tvArName: TextView

        init {
            tvArID = itemView.findViewById(R.id.tvProductId)
            tvArName = itemView.findViewById(R.id.tvNameArticulo)
        }
    }

    private var listener: (e: ProductData, position: Int) -> Unit = { e, position ->  }

    fun setOnClickListener(listener: (ProductData, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomArticulo.ViewHolderArticulo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_articulos, parent, false)
        val holder = ViewHolderArticulo(view)

        view.setOnClickListener{
            val position = holder.adapterPosition
            listener(listaArticulos[position], position)
        }
        return holder
    }

    override fun getItemCount(): Int = listaArticulos.size

    override fun onBindViewHolder(holder: CustomArticulo.ViewHolderArticulo, position: Int) {
        holder.tvArID.text = listaArticulos[position].id
        holder.tvArName.text = listaArticulos[position].name
    }
}
