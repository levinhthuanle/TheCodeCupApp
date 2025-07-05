package com.thecodecup.ui.rewards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.RewardItem
import kotlinx.coroutines.launch
import kotlin.math.min

class RewardsFragment : Fragment() {

    private lateinit var loyaltyContainer: LinearLayout
    private lateinit var txtPoints: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RewardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loyaltyContainer = view.findViewById(R.id.stampContainer)
        txtPoints       = view.findViewById(R.id.pointText)
        recyclerView    = view.findViewById(R.id.rewardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val dao       = AppDatabase.getInstance(requireContext()).orderDao()
            val rewardDao = AppDatabase.getInstance(requireContext()).rewardDao()
            val prefs     = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)

            // Build rewards list
            val historyOrders = dao.getOrdersByStatus("history")
            val rewards = historyOrders.flatMap { order ->
                List(order.quantity) {
                    RewardItem(order.name, order.timestamp, 12, order.imageResId)
                }
            }
            adapter = RewardAdapter(rewards)
            recyclerView.adapter = adapter

            // Calculate and display points
            val totalPoints   = dao.getTotalCupsByStatus("history") ?: 0
            val totalRedeemed = rewardDao.getAll().size
            txtPoints.text = (totalPoints * 12 - totalRedeemed * 20)
                .coerceAtLeast(0)
                .toString()

            // Load and draw stamps
            var stampCount = prefs.getInt("loyalty", 0)
            fun refresh() = drawStamps(stampCount)
            refresh()

            // On‐click: redeem and reset
            loyaltyContainer.setOnClickListener {
                if (stampCount >= 8) {
                    // Reset stored stamps
                    prefs.edit().putInt("loyalty", 0).apply()
                    stampCount = 0
                    refresh()
                    Toast.makeText(
                        requireContext(),
                        "Bạn đã đổi thưởng thành công! Stamps đã được đặt lại.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Cần đủ 8 stamps để đổi thưởng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Navigate to redeem screen
            view.findViewById<Button>(R.id.btnRedeem)
                .setOnClickListener { findNavController().navigate(R.id.redeemFragment) }
        }
    }

    private fun drawStamps(count: Int) {
        val max = 8
        loyaltyContainer.removeAllViews()
        val size = resources.getDimensionPixelSize(R.dimen.stamp_size)

        for (i in 1..max) {
            val img = ImageView(requireContext()).apply {
                setImageResource(if (i <= count) R.drawable.ic_stamp_filled else R.drawable.ic_stamp_empty)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(8, 0, 8, 0)
                }
            }
            loyaltyContainer.addView(img)
        }
        view?.findViewById<TextView>(R.id.txtLoyaltyProgress)
            ?.text = "${min(count, max)} / $max"
    }
}
