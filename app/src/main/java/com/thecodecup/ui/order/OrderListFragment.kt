package com.thecodecup.ui.order

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.OrderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private var orders = mutableListOf<OrderEntity>()

    private var status: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        status = arguments?.getString("status")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.orderRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Gọi hàm loadOrders để tải dữ liệu ban đầu
        loadOrders()
    }

    private fun loadOrders() {
        lifecycleScope.launch {
            val fetched = AppDatabase.getInstance(requireContext())
                .orderDao()
                .getOrdersByStatus(status ?: "ongoing")

            orders.clear()
            orders.addAll(fetched)

            adapter = OrderAdapter(orders) { order ->
                moveToHistory(order)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun moveToHistory(order: OrderEntity) {
        lifecycleScope.launch {
            AppDatabase.getInstance(requireContext())
                .orderDao()
                .updateStatus(order.id, "history")

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Đã chuyển sang History", Toast.LENGTH_SHORT).show()
                loadOrders() // Gọi lại để refresh danh sách sau khi cập nhật
            }
        }
    }

    companion object {
        fun newInstance(status: String): OrderListFragment {
            val fragment = OrderListFragment()
            val args = Bundle()
            args.putString("status", status)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onResume() {
        super.onResume()
        loadOrders()
    }

}
