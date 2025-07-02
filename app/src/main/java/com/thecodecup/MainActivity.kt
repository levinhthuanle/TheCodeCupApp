package com.thecodecup

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thecodecup.data.local.AppDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppDatabase.seedPromos(this)
        setContentView(R.layout.activity_main)

        // Lấy NavHostFragment và NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Ánh xạ các view
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val bgBottomNav = findViewById<FrameLayout>(R.id.bgNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.rewardsFragment -> {
                    navController.navigate(R.id.rewardsFragment)
                    true
                }
                R.id.ordersFragment -> {
                    navController.navigate(R.id.ordersFragment)
                    true
                }
                R.id.analyticsFragment -> {
                    navController.navigate(R.id.analyticsFragment)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.menu.findItem(destination.id)?.isChecked = true
        }

        // Lắng nghe thay đổi điều hướng để ẩn/hiện nav bar + thay đổi background
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment,
                R.id.cartFragment,
                R.id.orderSuccessFragment -> {
                    bottomNav.visibility = View.GONE
                    bgBottomNav.background = null
                }

                R.id.ordersFragment,
                R.id.rewardsFragment-> {
                    bottomNav.visibility = View.VISIBLE
                    bgBottomNav.background = null
                }

                else -> {
                    bottomNav.visibility = View.VISIBLE
                    bgBottomNav.setBackgroundResource(R.color.background_color)
                }
            }
        }
    }
}
