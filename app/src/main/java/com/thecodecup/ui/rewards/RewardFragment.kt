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

        txtPoints = view.findViewById(R.id.pointText)
        recyclerView = view.findViewById(R.id.rewardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).orderDao()
            val rewardDao = AppDatabase.getInstance(requireContext()).rewardDao()

            val historyOrders = dao.getOrdersByStatus("history")
            val rewards = mutableListOf<RewardItem>()
            for (order in historyOrders) {
                repeat(order.quantity) {
                    rewards.add(
                        RewardItem(order.name, order.timestamp, 12, order.imageResId)
                    )
                }
            }


            adapter = RewardAdapter(rewards)
            recyclerView.adapter = adapter

            // ✅ Điểm: tổng quantity (sản phẩm)
            val totalPoints = dao.getTotalCupsByStatus("history") ?: 0
            val totalRedeemed = rewardDao.getAll().size

            // totalCups * 12 - totalRedeems * 20
            txtPoints.text = (totalPoints * 12 - totalRedeemed * 20).coerceAtLeast(0).toString()

            // ✅ Stamp: số đơn hàng history
            val sharedPref = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val stampCount = sharedPref.getInt("loyalty", 0)
            drawStamps(stampCount)

            // Đổi thưởng: cần đủ 8 đơn hàng
            loyaltyContainer.setOnClickListener {
                if (stampCount >= 8) {
                    // Đổi thành công, reset stamps (nếu bạn muốn)
                    Toast.makeText(requireContext(), "Bạn đã đổi thưởng thành công!", Toast.LENGTH_SHORT).show()

                    // Optional: Reset lại stamps bằng cách xóa đơn trong DB nếu cần (hoặc flag thêm cột used)
                    // Ở đây chỉ Toast mà không reset thật vì bạn vẫn muốn giữ điểm
                } else {
                    Toast.makeText(requireContext(), "Cần đủ 8 stamps để đổi thưởng", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<Button>(R.id.btnRedeem).setOnClickListener {
                findNavController().navigate(R.id.redeemFragment)
            }
        }

    }

    private fun drawStamps(count: Int) {
        val max = 8
        loyaltyContainer.removeAllViews()
        val stampSize = resources.getDimensionPixelSize(R.dimen.stamp_size)

        for (i in 1..max) {
            val img = ImageView(requireContext())
            img.setImageResource(if (i <= count) R.drawable.ic_stamp_filled else R.drawable.ic_stamp_empty)
            img.layoutParams = LinearLayout.LayoutParams(stampSize, stampSize).apply {
                setMargins(8, 0, 8, 0)
            }
            loyaltyContainer.addView(img)
        }

        view?.findViewById<TextView>(R.id.txtLoyaltyProgress)?.text = "${min(count, max)} / $max"
    }
}
