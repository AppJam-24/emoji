package com.woojun.emoji.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApp : Application() {
    companion object {
        lateinit var prefs : MySharedPreferences
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
