package com.example.eanbapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.eanbapplication.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // Ensure that the NavHostFragment is correctly retrieved
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.event_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController != null) {
            // Setup BottomNavigationView with NavController only if navController is valid
            binding.bottomNavigation.setupWithNavController(navController)
            Log.e("HomeActivity", "NavController is set.")

        } else {
            Log.e("HomeActivity", "NavController is null, check fragment initialization.")
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_events -> {
                    navController?.navigate(R.id.eventsFragment)
                    Log.d("HomeActivity", "Navigated to EventsFragment")
                    true
                }
                R.id.nav_requests -> {
                    navController?.navigate(R.id.requestsFragment)
                    Log.d("HomeActivity", "Navigated to RequestsFragment")
                    true
                }
                R.id.nav_offers -> {
                    navController?.navigate(R.id.offersFragment)
                    Log.d("HomeActivity", "Navigated to OffersFragment")
                    true
                }
                else -> false
            }
        }




        //navController.navigate(R.id.eventsFragment)
    }


}