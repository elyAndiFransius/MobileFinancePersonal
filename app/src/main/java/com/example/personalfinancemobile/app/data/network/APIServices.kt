package com.example.personalfinancemobile.app.data.network

import android.telecom.CallScreeningService.CallResponse
import com.example.personalfinancemobile.app.data.model.Auth.loginRequest
import com.example.personalfinancemobile.app.data.model.Auth.loginResponse
import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.Target
import com.example.personalfinancemobile.app.data.model.TargetResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.personalfinancemobile.app.data.model.Target as TargetModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIServices {
    @POST("register")
    fun registerUser(@Body user: User): Call<User>


    @POST("login")
    fun login(@Body request: loginRequest): Call<loginResponse>

    @POST("budgets")
    fun createBudgetRequest(@Body budget: BudgetRequest): Call<ResponseBody>

    @Multipart
    @POST("targets")
    fun createTarget(
        @Part("gol") gol: RequestBody,
        @Part("targetAmount") targetAmount: RequestBody,
        @Part("currentAmount") currentAmount: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part file: MultipartBody.Part? // bisa null
    ): Call<ResponseBody>

    @GET("targets")
    fun getTarget(
        @Header("Authorization") token:String
    ): Call<TargetResponse>

}