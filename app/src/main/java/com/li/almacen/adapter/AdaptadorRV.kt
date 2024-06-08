package com.li.almacen.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.AlmacenData
import com.li.almacen.data.MovementData
import com.li.almacen.ui.almacen.details.DetailsAlmacen
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


class CustomAdapter (private var listaAlmacen : MutableList<AlmacenData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()


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
        val image: ImageView
        init {
            tvID = itemView.findViewById(R.id.idProducto)
            tvNombre = itemView.findViewById(R.id.productName)
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion)
            tvPopupMenu = itemView.findViewById(R.id.tvPopupMenu)
            image = itemView.findViewById(R.id.imageView)

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

    private var listener: (e: AlmacenData, position: Int) -> Unit = { _, _ ->  }
    fun setOnClickListener(listener:(AlmacenData, Int)->Unit){
        this.listener = listener
    }

    private var optionClickListener: (AlmacenData, Int) -> Unit = { _, _ -> }
    fun setOptionClickListener(listener: (AlmacenData, Int) -> Unit) {
        this.optionClickListener = listener
    }


    override fun getItemCount(): Int = listaAlmacen.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articulo = listaAlmacen[position]
        //inicio recyclerview con la informacion
        holder.tvID.text = listaAlmacen[position].id
        holder.tvNombre.text = listaAlmacen[position].name
        holder.tvUbicacion.text = listaAlmacen[position].ubicacion
        val uriString = articulo.uri as String?
        if (!uriString.isNullOrEmpty()) {
            Log.d("DetailsAlmacen", "Loading image from URI: $uriString")
            val uri = Uri.parse(uriString)
            Glide.with(holder.itemView.context)
                .load(uri)
                .error(R.drawable.png_almacen) // Imagen en caso de error
                .into(holder.image)
        } else {
            Log.d("DetailsAlmacen", "URI is null or empty, using default image.")
            holder.image.setImageResource(R.drawable.png_almacen) // Imagen por defecto
        }

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
                    intent.putExtra("ubicacion", listaAlmacen[position].ubicacion)
                    intent.putExtra("uri", listaAlmacen[position].uri.toString())
                    intent.putExtra("fecha", listaAlmacen[position].fechaCreacion.toString())
                    startActivity(context, intent, null)
                    true
                }
                R.id.action_delete -> {
                    db.collection("usuarios").document(userEmail!!)
                        .collection("productos_almacenes")
                        .whereEqualTo("almacenId", listaAlmacen[position].id)
                        .get()
                        .addOnSuccessListener { result ->
                            if (result != null && !result.isEmpty) {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                                builder.setMessage("El almacén seleccionado ya contiene productos. No se ha completado esta operación.")
                                    .setTitle("Error")
                                    .setPositiveButton("OK") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                                Log.e("Firestore", "Error al obtener datos. AdaptadorRV - 145")
                            } else {
                                val confirmBuilder : AlertDialog.Builder = AlertDialog.Builder(context)
                                confirmBuilder.setMessage("Seguro quieres eliminar este almacén?")
                                    .setTitle("Confirmar")
                                    .setPositiveButton("Eliminar") { dialog, which ->
                                        movementRegister(listaAlmacen[position].name, listaAlmacen[position].gerente)
                                        db.collection("usuarios").document(userEmail!!).collection("almacenes")
                                            .document(listaAlmacen[position].id!!)
                                            .delete()
                                            .addOnSuccessListener {
                                                listaAlmacen.removeAt(position)
                                                notifyItemRemoved(position)
                                                notifyItemRangeChanged(position, listaAlmacen.size)
                                            }
                                            .addOnFailureListener {
                                                Log.e("Firestore", "Error al borrar datos. AdaptadorRV - 157")
                                            }
                                    }
                                    .setNegativeButton("Cancelar") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                val confirmDialog: AlertDialog = confirmBuilder.create()
                                confirmDialog.show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error al obtener datos:", e)
                        }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    fun updateItem(newList: List<AlmacenData>) {
        this.listaAlmacen = newList.toMutableList()
        notifyDataSetChanged()
    }

    private fun movementRegister(name : String, gerente : String) {
        val nuevoMovimiento = MovementData(
            null, "Almacén $name", gerente, "Alta almacén", Date.from(LocalDate.now().atStartOfDay(
                ZoneId.systemDefault()).toInstant())
        )
        db.collection("usuarios").document(userEmail!!).collection("movimientos")
            .add(nuevoMovimiento)
            .addOnSuccessListener { documentReference ->
                nuevoMovimiento.id = documentReference.id
                documentReference.update("id", nuevoMovimiento.id)
            }
    }
}