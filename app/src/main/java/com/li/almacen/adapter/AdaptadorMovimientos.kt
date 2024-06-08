package com.li.almacen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.li.almacen.R
import com.li.almacen.data.MovementData

class MovementAdapter(private val movements: List<MovementData>) : RecyclerView.Adapter<MovementAdapter.MovementViewHolder>() {

    class MovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.tvIdMovement)
        val name: TextView = itemView.findViewById(R.id.tvNameMovement)
        val responsable: TextView = itemView.findViewById(R.id.tvResponsibleMovement)
        val tipoMovimiento: TextView = itemView.findViewById(R.id.tvTypeMovement)
        val fechaMovimiento: TextView = itemView.findViewById(R.id.tvDateMovement)
    }

    private var listener: (MovementData, Int) -> Unit = { _, _ -> }
    fun setOnClickListener(listener: (MovementData, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_movement, parent, false)
        val holder = MovementViewHolder(view)

        view.setOnClickListener {
            val position = holder.adapterPosition
            listener(movements[position], position)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movement = movements[position]
        holder.id.text = movement.id
        holder.name.text = movement.name
        holder.responsable.text = movement.manager
        holder.tipoMovimiento.text = movement.movementType
        holder.fechaMovimiento.text = movement.fecha.toString()
    }

    override fun getItemCount(): Int = movements.size
}
