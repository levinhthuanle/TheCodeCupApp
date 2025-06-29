package com.thecodecup.ui.home

import Coffee
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R

class CoffeeAdapter(
    private val coffees: List<Coffee>,
    private val onItemClick: (Coffee) -> Unit
) : RecyclerView.Adapter<CoffeeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.coffeeName)

        val image: ImageView = itemView.findViewById(R.id.coffeeImage)

        fun bind(coffee: Coffee) {
            name.text = coffee.name

            image.setImageResource(coffee.imageResId)
            itemView.setOnClickListener { onItemClick(coffee) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_coffee, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(coffees[position])
    override fun getItemCount() = coffees.size
}
