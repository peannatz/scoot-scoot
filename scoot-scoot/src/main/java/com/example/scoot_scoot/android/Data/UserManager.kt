package com.example.scoot_scoot.android.Data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.util.concurrent.TimeUnit

object UserManager {
    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_LOCATION_PERMISSIONS = "location_permissions_granted"
    private const val KEY_USER_TUTORIAL = "tutorial"

    private const val KEY_FIRST_ATTEMPT_TIMESTAMP = "lastAttemptTimestamp"
    private const val KEY_WRONG_ATTEMPTS = "amountWrongAttempts"

    val hourInMillis = TimeUnit.MINUTES.toMillis(60)


    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    fun savePermissionsStatus(granted: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_LOCATION_PERMISSIONS, granted)
            .apply()
    }

    fun getPermissionsStatus():Boolean {
        return sharedPreferences.getBoolean(KEY_LOCATION_PERMISSIONS, false)
    }

    fun saveUser(user: UserData) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, user.id.toString())
            apply()
            putString(KEY_USER_NAME, user.surname)
            apply()
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

    fun dontShowTutorial() {
        sharedPreferences.edit().putBoolean(KEY_USER_TUTORIAL, true).apply()
    }

    fun getFirstWrongAttempt(): Long {
        return sharedPreferences.getLong(KEY_FIRST_ATTEMPT_TIMESTAMP, 0)
    }

    fun checkIfUserHasAttempts(): Boolean {
        val firstAttempt = getFirstWrongAttempt()
        val now = System.currentTimeMillis()
        if (now - firstAttempt >= hourInMillis) {
            clearWrongAttempts()
        } else if (getWrongAttempts() >= 3) {
            return false
        }
        return true
    }

    fun getWrongAttempts(): Int {
        return sharedPreferences.getInt(KEY_WRONG_ATTEMPTS, 0)
    }

    fun clearWrongAttempts() {
        sharedPreferences.edit {
            putLong(KEY_FIRST_ATTEMPT_TIMESTAMP, 0)
            putLong(KEY_WRONG_ATTEMPTS, 0)
            apply()
        }
    }

    fun saveWrongAttempt() {
        if (getFirstWrongAttempt() < 1) {
            saveWrongAttemptTime()
        }
        val oldAmount = getWrongAttempts()
        sharedPreferences.edit().putInt(KEY_WRONG_ATTEMPTS, oldAmount + 1).apply()
    }

    private fun saveWrongAttemptTime() {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().putLong(KEY_FIRST_ATTEMPT_TIMESTAMP, currentTime).apply()
    }

    fun showTutorial(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_TUTORIAL, true)
    }

    fun clearUser() {
        sharedPreferences.edit().clear().apply()
    }

}