package com.example.scoot_scoot.android.Data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserManager {
    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(userId: String, userName: String) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != null
    }

    fun clearUser() {
        sharedPreferences.edit().clear().apply()
    }

}