package com.li.almacen.kt

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.ui.almacen.details.DetailsAlmacen


class CustomAdapter (private var listaAlmacen : MutableList<AlmacenData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class EstanteriaDiffCallback(val oldList: MutableList<AlmacenData>, val newList: MutableList<AlmacenData>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvID : TextView
        val tvNombre : TextView
        val tvUbicacion : TextView
        val tvPopupMenu : ImageView
        init {
            tvID = itemView.findViewById(R.id.idProducto)
            tvNombre = itemView.findViewById(R.id.productName)
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion)
            tvPopupMenu = itemView.findViewById(R.id.tvPopupMenu)

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

    private var listener: (e: AlmacenData, position: Int) -> Unit = { e, position ->  }
    fun setOnClickListener(listener:(AlmacenData, Int)->Unit){
        this.listener = listener
    }

    private var optionClickListener: (AlmacenData, Int) -> Unit = { _, _ -> }
    fun setOptionClickListener(listener: (AlmacenData, Int) -> Unit) {
        this.optionClickListener = listener
    }

    override fun getItemCount(): Int = listaAlmacen.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //inicio recyclerview con la informacion
        holder.tvID.text = listaAlmacen[position].id
        holder.tvNombre.text = listaAlmacen[position].name
        holder.tvUbicacion.text = listaAlmacen[position].ubicacion

        //inicio accion clickable
        holder.itemView.setOnClickListener{
            listener(listaAlmacen[position], position)
        }

        holder.tvPopupMenu.setOnClickListener{ view ->
            showPopupMenu(view, holder.itemView.context, position)
        }
    }

    fun updateList(newList: MutableList<AlmacenData>) {
        val estanteriaDiff = EstanteriaDiffCallback(listaAlmacen,newList)
        val result = DiffUtil.calculateDiff(estanteriaDiff)
        listaAlmacen = newList
        result.dispatchUpdatesTo(this)
    }

    private fun showPopupMenu(view: View, context: Context, position: Int) {
        val wrapper = ContextThemeWrapper(context, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.dropdown_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(context, DetailsAlmacen::class.java)
                    intent.putExtra("id", listaAlmacen[position].id)
                    intent.putExtra("name", listaAlmacen[position].name)
                    intent.putExtra("description", listaAlmacen[position].notas)
                    intent.putExtra("encargado", listaAlmacen[position].gerente)
                    intent.putExtra("capacidad", listaAlmacen[position].capacidad)
                    intent.putExtra("ubicacion", listaAlmacen[position].ubicacion)
                    intent.putExtra("uri", listaAlmacen[position].uri.toString())


                    startActivity(context, intent, null)
                    true
                }
                R.id.action_delete -> {
                    // Handle delete action
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}