package com.thecodecup.ui.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.CartItemEntity
import com.thecodecup.data.model.OrderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {

    private lateinit var adapter: CartAdapter
    private lateinit var totalText: TextView

    // giờ dùng list entity thực
    private val cartItems = mutableListOf<CartItemEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        totalText = view.findViewById(R.id.totalPrice)

        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CartAdapter(cartItems) { item ->
            // xóa item khỏi DB và cập nhật UI
            lifecycleScope.launch {
                AppDatabase.getInstance(requireContext())
                    .cartDao()
                    .delete(item)
                loadCartItems()
            }
        }

        recyclerView.adapter = adapter

        // load dữ liệu từ Room
        loadCartItems()

        view.findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Proceeding to checkout", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(requireContext())
                val cartItems = db.cartDao().getAll()

                for (item in cartItems) {
                    db.orderDao().insert(
                        OrderEntity(
                            name = item.name,
                            imageResId = item.imageResId,
                            description = "${item.shot} | ${item.type} | ${item.size} | ${item.ice}",
                            quantity = item.quantity,
                            price = item.price,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                db.cartDao().clearAll()

                // Update loyalty stamps (tăng số lượng)
                val sharedPref = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val old = sharedPref.getInt("loyalty", 0)
                sharedPref.edit().putInt("loyalty", (old + 1).coerceAtMost(8)).apply()

                findNavController().navigate(R.id.orderSuccessFragment)
            }
        }



        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun loadCartItems() {
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(requireContext())
                    .cartDao()
                    .getAll()
            }
            cartItems.clear()
            cartItems.addAll(items)
            adapter.notifyDataSetChanged()
            updateTotal()
        }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        totalText.text = String.format("$%.2f", total)
    }
}
