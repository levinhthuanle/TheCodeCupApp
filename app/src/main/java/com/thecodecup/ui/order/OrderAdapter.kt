package com.thecodecup.ui.order

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.model.OrderEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(
    private val orders: List<OrderEntity>,
    private val onItemClick: (OrderEntity) -> Unit = {}
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtDesc: TextView = itemView.findViewById(R.id.txtDesc)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val img: ImageView = itemView.findViewById(R.id.imgOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.txtName.text = order.name
        holder.txtDesc.text = order.address

        holder.txtPrice.text = "$${order.price * order.quantity}"
        holder.txtQuantity.text = "Quantity: ${order.quantity}"
        holder.txtTime.text = SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault()).format(Date())
        holder.img.setImageResource(order.imageResId)

        // Gắn sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick(order)
        }
    }

    override fun getItemCount(): Int = orders.size
}
