package com.example.offersatyoursteps.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context : Context) {
    
    val PREFS_FILENAME = "prefs"
    val prefs : SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    
    val IS_LOGGED_IN ="isLoggedIn"
    
    var isLoggedIn:Boolean
        get() =prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) =prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
}