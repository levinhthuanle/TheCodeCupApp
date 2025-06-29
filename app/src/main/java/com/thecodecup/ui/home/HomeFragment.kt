package com.thecodecup.ui.home

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecodecup.R
import Coffee
import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thecodecup.data.local.AppDatabase
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CoffeeAdapter
    private lateinit var stampContainer: LinearLayout
    private lateinit var txtLoyaltyProgress: TextView
    private lateinit var btnCart: ImageButton
    private lateinit var btnProfile: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Init views
        recyclerView = view.findViewById(R.id.coffeeRecyclerView)
        stampContainer = view.findViewById(R.id.stampContainer)
        txtLoyaltyProgress = view.findViewById(R.id.txtLoyaltyProgress)
        btnCart = view.findViewById(R.id.btnCart)
        btnProfile = view.findViewById(R.id.btnProfile)

        // Setup coffee list as grid
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val sampleCoffees = listOf(
            Coffee(1, "Americano",  4.0, R.drawable.americano),
            Coffee(2, "Cappuccino",  4.5, R.drawable.cappuccino),
            Coffee(3, "Mocha", 5.0, R.drawable.mocha),
            Coffee(4, "Flat White", 4.8, R.drawable.flat_white)
        )

        adapter = CoffeeAdapter(sampleCoffees) { coffee ->
            val bundle = Bundle().apply {
                putParcelable("coffee", coffee)
            }
            findNavController().navigate(R.id.detailsFragment, bundle)
        }



        recyclerView.adapter = adapter

        // Setup loyalty card
        val sharedPref = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val stampCount = sharedPref.getInt("loyalty", 0)
        val maxStamps = 8
        txtLoyaltyProgress.text = "$stampCount / $maxStamps"
        drawLoyaltyStamps(stampCount, maxStamps)

        // Optional: handle buttons
        btnCart.setOnClickListener {
            // TODO: chuyển sang màn hình Cart
            // TODO: chuyển sang màn hình Cart
            findNavController().navigate(R.id.cartFragment)
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        val txtHello = view.findViewById<TextView>(R.id.txtHello)

        lifecycleScope.launch {
            val profile = AppDatabase.getInstance(requireContext()).userProfileDao().getProfile()
            val name = profile?.name ?: "Anderson"
            txtHello.text = "$name"
        }


        return view
    }

    private fun drawLoyaltyStamps(current: Int, total: Int) {
        stampContainer.removeAllViews()
        for (i in 1..total) {
            val stamp = ImageView(requireContext())
            stamp.setImageResource(
                if (i <= current) R.drawable.ic_stamp_filled else R.drawable.ic_stamp_empty
            )
            val size = resources.getDimensionPixelSize(R.dimen.stamp_size)
            val params = LinearLayout.LayoutParams(size, size)
            params.setMargins(8, 0, 8, 0)
            stamp.layoutParams = params
            stampContainer.addView(stamp)
        }
    }
}
