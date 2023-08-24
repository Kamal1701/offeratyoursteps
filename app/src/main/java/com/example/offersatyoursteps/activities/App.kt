package com.example.offersatyoursteps.activities

import android.app.Application
import com.example.offersatyoursteps.utilities.SharedPrefs

class App : Application() {
    companion object {
        lateinit var sharedPrefs : SharedPrefs
    }
    
    override fun onCreate() {
        sharedPrefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}