package com.li.almacen.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.li.almacen.R


class ProductAdapter(private var listaArticulos: MutableList<Articulos>) : RecyclerView.Adapter<ProductAdapter.ViewHolderProducto>() {

    class ViewHolderProducto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArID: TextView
        val tvArName: TextView

        init {
            tvArID = itemView.findViewById(R.id.idProducto)
            tvArName = itemView.findViewById(R.id.productName)
        }
    }

    private var listener: (e: Articulos, position: Int) -> Unit = { e, position ->  }

    fun setOnClickListener(listener: (Articulos, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolderProducto {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_productos, parent, false)
        val holder = ViewHolderProducto(view)

        view.setOnClickListener{
            val position = holder.adapterPosition
            listener(listaArticulos[position], position)
        }
        return holder
    }

    override fun getItemCount(): Int = listaArticulos.size

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolderProducto, position: Int) {
        holder.tvArID.text = listaArticulos[position].id
        holder.tvArName.text = listaArticulos[position].nombre
    }
}
