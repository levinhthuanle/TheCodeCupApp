package com.thecodecup.ui.rewards

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.model.RewardItem

class RedeemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_redeem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.redeemRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Mock data (sau có thể lấy từ DB hoặc API)
        val redeemableItems = listOf(
            RewardItem("Cafe Latte", System.currentTimeMillis(), 1340),
            RewardItem("Flat White", System.currentTimeMillis(), 1340),
            RewardItem("Cappuccino", System.currentTimeMillis(), 1340),
        )

        recyclerView.adapter = RedeemAdapter(redeemableItems)
    }
}
