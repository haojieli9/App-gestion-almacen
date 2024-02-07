package com.li.almacen.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.li.almacen.R

class CustomAdapter (private var listaAlmacen : List<Almacenes>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvID : TextView
        val tvNombre : TextView
        val tvUbicacion : TextView
        val imgV : ImageView
        init {
            tvID = itemView.findViewById(R.id.tvID)
            tvNombre = itemView.findViewById(R.id.tvNombre)
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion)
            imgV = itemView.findViewById(R.id.imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_almacen, parent, false)
        val holder = ViewHolder(view)

        view.setOnClickListener{
            val position = holder.adapterPosition
            listener(listaAlmacen[position], position)
        }
        return ViewHolder(view)
    }

    private var listener: (e: Almacenes, position: Int) -> Unit = { e, position ->  }
    fun setOnClickListener(listener:(Almacenes, Int)->Unit){
        this.listener = listener
    }
    override fun getItemCount(): Int = listaAlmacen.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //inicio recyclerview con la informacion
        holder.tvID.text = listaAlmacen[position].id
        holder.tvNombre.text = listaAlmacen[position].nombre
        holder.tvUbicacion.text = listaAlmacen[position].ubicacion

        //libreria GLIDE para cargar imagenes
        Glide.with(holder.itemView.context)
            .load(listaAlmacen[position].cover)
            .into(holder.imgV)

        //inicio accion clickable
        holder.itemView.setOnClickListener{
            listener(listaAlmacen[position], position)
        }
    }
}