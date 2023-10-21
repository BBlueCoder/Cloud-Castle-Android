package com.bluecoder.cloudcastle.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesManager(private val context : Context) {

    private val KEY = "CLOUD_CASTLE_PREFERENCES_KEY"

    companion object {
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val TOKEN_KEY = "token"
    }
    fun addPreference(key : String, value : String) {
        val pref = context.getSharedPreferences(KEY,Context.MODE_PRIVATE)

        with(pref.edit()){
            putString(key,value)
            apply()
        }
    }

    fun getPreference(key : String) : String? {
        val pref = context.getSharedPreferences(KEY,Context.MODE_PRIVATE)

        return pref.getString(key,null)
    }
}