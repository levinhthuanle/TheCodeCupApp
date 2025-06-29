package com.thecodecup.ui.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.model.RewardItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RewardAdapter(private val items: List<RewardItem>) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDrinkName: TextView = view.findViewById(R.id.txtDrinkName)
        val txtOrderTime: TextView = view.findViewById(R.id.txtOrderTime)
        val txtPoints: TextView = view.findViewById(R.id.txtPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val item = items[position]
        holder.txtDrinkName.text = item.name
        holder.txtOrderTime.text = formatTimestamp(item.timestamp)
        holder.txtPoints.text = "+${item.points} Pts"
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM | hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
