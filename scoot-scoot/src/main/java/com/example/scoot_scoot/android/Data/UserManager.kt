package com.example.scoot_scoot.android.Data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.TimeUnit

object UserManager {
    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"
    private const val KEY_LOCATION_PERMISSIONS = "location_permissions_granted"
    private const val KEY_USER_TUTORIAL = "tutorial"
    private const val KEY_RIDE_START = "ride_Start_time"

    private const val KEY_FIRST_ATTEMPT_TIMESTAMP = "lastAttemptTimestamp"
    private const val KEY_WRONG_ATTEMPTS = "amountWrongAttempts"

    val hourInMillis = TimeUnit.MINUTES.toMillis(60)


    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //PERMISSIONS

    fun savePermissionsStatus(granted: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_LOCATION_PERMISSIONS, granted)
            .apply()
    }

    fun getPermissionsStatus(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOCATION_PERMISSIONS, false)
    }

    //TIME

    fun saveStartTime() {
        var currentTime = Date.from(Instant.now())
        var millis = currentTime.time

        sharedPreferences.edit()
            .putLong(KEY_RIDE_START, millis)
            .apply()
    }

    fun getStartTime(): Date {
        val millis = sharedPreferences.getLong(KEY_RIDE_START, 0)
        val date = Date.from(Instant.ofEpochMilli(millis))
        return date
    }

    fun clearStartTime() {
        sharedPreferences.edit().remove(KEY_RIDE_START).apply()
    }

    //USER

    fun saveUser(user: UserData) {
        sharedPreferences.edit {
            putInt(KEY_USER_ID, user.id!!)
            apply()
            putString(KEY_USER_NAME, user.surname)
            apply()
        }
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1
    }

    fun clearUser() {
        sharedPreferences.edit().clear().apply()
    }

    //TUTORIAL
    fun dontShowTutorial() {
        sharedPreferences.edit().putBoolean(KEY_USER_TUTORIAL, true).apply()
    }

    fun showTutorial(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_TUTORIAL, true)
    }

    //ALC TEST

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
            remove(KEY_FIRST_ATTEMPT_TIMESTAMP)
            remove(KEY_WRONG_ATTEMPTS)
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

}