package com.example.easytogo.view.activity

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.easytogo.R
import com.example.easytogo.base.BasePermissionActivity
import com.example.easytogo.base.CallBack
import com.example.easytogo.view.fragmentNavigator.FixFragmentNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BasePermissionActivity(), CallBack {
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moudel_activity_main)
        navigationView = findViewById(R.id.nav_view)
        var navController = findNavController(R.id.nav_container)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        val homeFragmentProvider =
            FixFragmentNavigator(
                this,
                supportFragmentManager,
                fragment.id
            )
        navController.navigatorProvider.addNavigator(homeFragmentProvider)
        navController.setGraph(R.navigation.nav_fragments);
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    @Override
    override fun call(arg: Bundle?) {
        if (arg?.getString("msg").equals("Hide")) {
            navigationView.visibility = View.GONE
        } else if (arg?.getString("msg").equals("Show")) {
            navigationView.visibility = View.VISIBLE
        }
    }
}
