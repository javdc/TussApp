package com.javdc.tussapp.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.mainBottomNavigationView?.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { item ->
                val selectedMenuItemNavGraph = navHostFragment.navController.graph.findNode(item.itemId) as? NavGraph?
                selectedMenuItemNavGraph?.let { menuGraph ->
                    navController.popBackStack(menuGraph.startDestinationId, false)
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding?.mainBottomNavigationView?.visibility = when (destination.id) {
                R.id.aboutFragment -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}