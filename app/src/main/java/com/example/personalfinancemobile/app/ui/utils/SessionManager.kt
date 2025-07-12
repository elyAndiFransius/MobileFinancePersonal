package com.example.personalfinancemobile.app.ui.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        private const val PREF_NAME = "PersonalFinanceSession"
        private const val KEY_AUTH_TOKEN = "TOKEN"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "id"
        private const val KEY_USER_NAME = "name"
        private const val KEY_USER_EMAIL = "email"
    }

    /**
     * Menyimpan token autentikasi
     */
    fun saveAuthToken(token: String) {
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    /**
     * Mengambil token autentikasi
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Menyimpan data user
     */
    fun saveUserData(userId: String, userName: String, userEmail: String) {
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
    }

    /**
     * Mengambil User ID
     */
    fun fetchUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    /**
     * Mengambil User Name
     */
    fun fetchUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    /**
     * Mengambil User Email
     */
    fun fetchUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    /**
     * Cek apakah user sudah login
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Logout user - hapus semua data session
     */
    fun logout() {
        editor.clear()
        editor.apply()
    }

    /**
     * Hapus hanya auth token
     */
    fun clearAuthToken() {
        editor.remove(KEY_AUTH_TOKEN)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()
    }
}