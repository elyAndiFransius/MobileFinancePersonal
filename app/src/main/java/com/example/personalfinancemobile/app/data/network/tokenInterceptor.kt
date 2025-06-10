package com.example.personalfinancemobile.app.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class tokenInterceptor (private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharePref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token  = sharePref.getString("TOKEN", null)

        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}