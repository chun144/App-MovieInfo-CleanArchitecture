package com.congvtt1.smartmovie.movie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationView()

        setOnSelectedItem()
    }

    private fun setupBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }

    private fun setOnSelectedItem() {
        binding.bottomNavigationView.setOnItemSelectedListener  { menuItem ->
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    menuItem.isChecked = true
                    NavigationUI.onNavDestinationSelected(menuItem, navHostFragment.navController)
                    true
                }
                R.id.searchFragment -> {
                    menuItem.isChecked = true
                    NavigationUI.onNavDestinationSelected(menuItem, navHostFragment.navController)
                    true
                }
                R.id.genresFragment -> {
                    menuItem.isChecked = true
                    NavigationUI.onNavDestinationSelected(menuItem, navHostFragment.navController)
                    true
                }
                else -> false
            }
        }
    }
}