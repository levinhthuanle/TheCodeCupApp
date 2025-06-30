package com.thecodecup.ui.rewards

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.RewardHistoryEntity
import com.thecodecup.data.model.RewardItem
import kotlinx.coroutines.launch

class RedeemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_redeem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.redeemRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val redeemableItems = listOf(
            RewardItem("Americano", System.currentTimeMillis(), 20, R.drawable.americano),
            RewardItem("Flat White", System.currentTimeMillis(), 20, R.drawable.flat_white),
            RewardItem("Cappuccino", System.currentTimeMillis(), 20, R.drawable.cappuccino),
            RewardItem("Mocha", System.currentTimeMillis(), 20, R.drawable.mocha)
        )

        recyclerView.adapter = RedeemAdapter(redeemableItems) { item ->
            handleRedeem(item)
        }
    }

    private fun handleRedeem(item: RewardItem) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val orderDao = db.orderDao()
            val rewardDao = db.rewardDao()

            val totalCups = orderDao.getTotalCupsByStatus("history") ?: 0
            val totalRedeems = rewardDao.getAll().size
            val currentPoints = totalCups * 12 - totalRedeems * 20




            if (currentPoints >= 20) {
                // Ghi vào lịch sử redeem
                rewardDao.insert(RewardHistoryEntity(0, item.name, System.currentTimeMillis()))
                Toast.makeText(requireContext(),
                    "Đã đổi thành công ${item.name}.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(),
                    "Không đủ điểm để đổi ${item.name}. Cần thêm ${20 - currentPoints} điểm.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
