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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.li.almacen.R
import com.li.almacen.data.MovementData
import com.li.almacen.data.ProductData
import com.li.almacen.ui.productos.ProductViewModel
import com.li.almacen.ui.productos.details.DetailProduct
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class ProductAdapter(private val productViewModel: ProductViewModel, private var listaArticulos: MutableList<ProductData>) : RecyclerView.Adapter<ProductAdapter.ViewHolderArticulo2>() {
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email
    private var db = FirebaseFirestore.getInstance()
    var nombre : String = ""

    inner class ArticuloDiffCallback(private val oldList: MutableList<ProductData>, private val newList: MutableList<ProductData>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ViewHolderArticulo2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArID: TextView
        val tvArName: TextView
        val tvProCant: TextView
        val tvPrecio: TextView
        val image: ImageView
        val menuoption: ImageView

        init {
            tvArID = itemView.findViewById(R.id.tvProductId)
            tvArName = itemView.findViewById(R.id.tvNameArticulo)
            tvProCant = itemView.findViewById(R.id.tvProCant)
            tvPrecio = itemView.findViewById(R.id.tvPrecio)
            image = itemView.findViewById(R.id.imgArticulo)
            menuoption = itemView.findViewById(R.id.imgOption)
        }
    }

    private var listener: (e: ProductData, position: Int) -> Unit = { _, _ ->  }
    fun setOnClickListener(listener: (ProductData, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderArticulo2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_articulos, parent, false)
        val holder = ViewHolderArticulo2(view)

        view.setOnClickListener{
            val position = holder.adapterPosition
            listener(listaArticulos[position], position)
        }
        return holder
    }

    override fun getItemCount(): Int = listaArticulos.size

    override fun onBindViewHolder(holder: ViewHolderArticulo2, position: Int) {
        val articulo = listaArticulos[position]

        holder.tvArID.text = listaArticulos[position].barcode
        holder.tvArName.text = listaArticulos[position].name
        holder.tvProCant.text = listaArticulos[position].cantidad
        holder.tvPrecio.text = listaArticulos[position].venta

        holder.menuoption.setOnClickListener { view ->
            showPopupMenu(view, holder.itemView.context, position)
        }

        val uriString = articulo.uri as String?
        if (!uriString.isNullOrEmpty()) {
            val uri = Uri.parse(uriString)
            Glide.with(holder.itemView.context)
                .load(uri)
                .error(R.drawable.product_file)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.product_file)
        }
    }

    private fun showPopupMenu(view: View, context: Context, position: Int) {
        val wrapper = ContextThemeWrapper(context, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.dropdown_menu_product, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(context, DetailProduct::class.java)
                    intent.putExtra("idProd", listaArticulos[position].id)
                    intent.putExtra("nameProd", listaArticulos[position].name)
                    intent.putExtra("barcodeProd", listaArticulos[position].barcode)
                    intent.putExtra("almacenProd", listaArticulos[position].almacenDestino)
                    intent.putExtra("categoriaProd", listaArticulos[position].categoria)
                    intent.putExtra("proveedorProd", listaArticulos[position].proveedor)
                    intent.putExtra("cantidadProd", listaArticulos[position].cantidad)
                    intent.putExtra("costeProd", listaArticulos[position].coste)
                    intent.putExtra("ventaProd", listaArticulos[position].venta)
                    intent.putExtra("descripProd", listaArticulos[position].descriptor)
                    intent.putExtra("fechaVencimientoProd", listaArticulos[position].fechaVencimiento)
                    intent.putExtra("uriProd", listaArticulos[position].uri as String)
                    ContextCompat.startActivity(context, intent, null)
                    true
                }
                R.id.action_delete -> {
                    db.collection("usuarios").document(userEmail!!)
                        .collection("productos_almacenes")
                        .whereEqualTo("productoId", listaArticulos[position].id)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                val documentId = querySnapshot.documents[0].id
                                val confirmBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                                confirmBuilder.setMessage("Seguro quieres eliminar este producto?")
                                    .setTitle("Confirmar")
                                    .setPositiveButton("Eliminar") { _, _ ->
                                        movementRegister(listaArticulos[position].name!!)
                                        db.collection("usuarios").document(userEmail!!).collection("productos_almacenes")
                                            .document(documentId)
                                            .delete()
                                            .addOnSuccessListener {
                                                listaArticulos.removeAt(position)
                                                notifyItemRemoved(position)
                                                notifyItemRangeChanged(position, listaArticulos.size)
                                                productViewModel.setProductList(listaArticulos)
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Firestore", "Error al borrar datos. AdaptadorArticulos - 150", e)
                                            }
                                    }
                                    .setNegativeButton("Cancelar") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                val confirmDialog: AlertDialog = confirmBuilder.create()
                                confirmDialog.show()
                            } else {
                                Log.e("Firestore", "No se encontró el documento con productoId ${listaArticulos[position].id}")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error al obtener datos. AdaptadorArticulos - 150", e)
                        }

                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    fun updateList(newList: MutableList<ProductData>) {
        val estanteriaDiff = ArticuloDiffCallback(listaArticulos,newList)
        val result = DiffUtil.calculateDiff(estanteriaDiff)
        listaArticulos = newList
        result.dispatchUpdatesTo(this)
    }

    private fun movementRegister(name : String) {
        val nuevoMovimiento = MovementData(
            null, "Producto $name", "Administrador", "Borrado registro", Date.from(
                LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        )
        db.collection("usuarios").document(userEmail!!).collection("movimientos")
            .add(nuevoMovimiento)
            .addOnSuccessListener { documentReference ->
                nuevoMovimiento.id = documentReference.id
                documentReference.update("id", nuevoMovimiento.id)
            }
    }
}
