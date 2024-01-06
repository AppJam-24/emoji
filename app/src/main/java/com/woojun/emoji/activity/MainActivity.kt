package com.woojun.emoji.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.woojun.emoji.R
import com.woojun.emoji.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigation.setItemSelected(R.id.home)

            bottomNavigation.setOnItemSelectedListener {
                when (it) {
                    R.id.home -> navController.navigate(R.id.home)
                    R.id.chat -> navController.navigate(R.id.chat)
                    R.id.setting -> navController.navigate(R.id.setting)
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigation.setItemSelected(destination.id)
            }

        }
    }
}