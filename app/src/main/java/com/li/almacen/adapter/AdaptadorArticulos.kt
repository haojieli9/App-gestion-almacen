package com.li.almacen.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.ProductData

class CustomArticulo(private var listaArticulos: MutableList<ProductData>) : RecyclerView.Adapter<CustomArticulo.ViewHolderArticulo>() {


    inner class ArticuloDiffCallback(val oldList: MutableList<ProductData>, val newList: MutableList<ProductData>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ViewHolderArticulo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArID: TextView
        val tvArName: TextView
        val tvProCant: TextView
        val tvPrecio: TextView
        val image: ImageView

        init {
            tvArID = itemView.findViewById(R.id.tvProductId)
            tvArName = itemView.findViewById(R.id.tvNameArticulo)
            tvProCant = itemView.findViewById(R.id.tvProCant)
            tvPrecio = itemView.findViewById(R.id.tvPrecio)
            image = itemView.findViewById(R.id.imgArticulo)
        }
    }

    private var listener: (e: ProductData, position: Int) -> Unit = { e, position ->  }

    fun setOnClickListener(listener: (ProductData, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderArticulo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_articulos, parent, false)
        val holder = ViewHolderArticulo(view)

        view.setOnClickListener{
            val position = holder.adapterPosition
            listener(listaArticulos[position], position)
        }
        return holder
    }

    override fun getItemCount(): Int = listaArticulos.size

    override fun onBindViewHolder(holder: ViewHolderArticulo, position: Int) {
        val articulo = listaArticulos[position]

        holder.tvArID.text = listaArticulos[position].id
        holder.tvArName.text = listaArticulos[position].name
        holder.tvProCant.text = listaArticulos[position].cantidad
        holder.tvPrecio.text = listaArticulos[position].venta

        val uriString = articulo.uri as String?
        if (!uriString.isNullOrEmpty()) {
            Log.d("DetailsAlmacen", "Loading image from URI: $uriString")
            val uri = Uri.parse(uriString)
            Glide.with(holder.itemView.context)
                .load(uri)
                .error(R.drawable.product_file) // Imagen en caso de error
                .into(holder.image)
        } else {
            Log.d("DetailsAlmacen", "URI is null or empty, using default image.")
            holder.image.setImageResource(R.drawable.product_file) // Imagen por defecto
        }
    }

    fun updateList(newList: MutableList<ProductData>) {
        val estanteriaDiff = ArticuloDiffCallback(listaArticulos,newList)
        val result = DiffUtil.calculateDiff(estanteriaDiff)
        listaArticulos = newList
        result.dispatchUpdatesTo(this)
    }
}
