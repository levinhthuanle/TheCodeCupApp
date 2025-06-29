package com.thecodecup.ui.rewards
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.model.RewardItem

class RedeemAdapter(private val items: List<RewardItem>) : RecyclerView.Adapter<RedeemAdapter.RedeemViewHolder>() {

    class RedeemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCoffee: ImageView = view.findViewById(R.id.imgCoffee)
        val txtCoffeeName: TextView = view.findViewById(R.id.txtCoffeeName)
        val txtValidity: TextView = view.findViewById(R.id.txtValidity)
        val txtPointCost: TextView = view.findViewById(R.id.txtPointCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedeemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_redeem, parent, false)
        return RedeemViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RedeemViewHolder, position: Int) {
        val item = items[position]
        holder.txtCoffeeName.text = item.name
        holder.txtValidity.text = "Valid until 04.07.21"
        holder.txtPointCost.text = "${item.points} pts"
        // TODO: add proper image for each coffee
        holder.imgCoffee.setImageResource(R.drawable.cappuccino) // temp placeholder
    }
}
