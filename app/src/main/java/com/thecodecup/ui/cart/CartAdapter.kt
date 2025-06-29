package com.thecodecup.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.model.CartItem
import com.thecodecup.data.model.CartItemEntity

class CartAdapter(
    private val items: MutableList<CartItemEntity>,
    private val onDelete: (CartItemEntity) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val name: TextView = view.findViewById(R.id.itemName)
        val options: TextView = view.findViewById(R.id.itemOptions)
        val quantity: TextView = view.findViewById(R.id.itemQuantity)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(item: CartItemEntity) {
            image.setImageResource(item.imageResId)
            name.text = item.name
            options.text = "${item.shot} | ${item.type} | ${item.size} | ${item.ice}"
            quantity.text = "x${item.quantity}"
            price.text = "$${item.price * item.quantity}"

            btnDelete.setOnClickListener {
                onDelete(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    fun remove(item: CartItemEntity) {
        val index = items.indexOf(item)
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun total(): Double = items.sumOf { it.price * it.quantity }
}
