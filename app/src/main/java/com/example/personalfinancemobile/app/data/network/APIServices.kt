package com.example.personalfinancemobile.app.data.network

import android.telecom.CallScreeningService.CallResponse
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.model.Auth.loginRequest
import com.example.personalfinancemobile.app.data.model.Auth.loginResponse
import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.Target
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.model.TransactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.personalfinancemobile.app.data.model.Target as TargetModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIServices {

    // Authentifikasi
    @POST("register")
    fun registerUser(@Body user: User): Call<User>

    @POST("login")
    fun login(@Body request: loginRequest): Call<loginResponse>

    // Budget
    @POST("budgets/create")
    fun createBudgetRequest(
        @Body budget: BudgetRequest,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @GET("budgets")
    fun budgetingIndex(
        @Header("Authorization") token: String
    ): Call<BudgetingResponse>

    // Target
    @Multipart
    @POST("targets")
    fun createTarget(
        @Part("gol") gol: RequestBody,
        @Part("targetAmount") targetAmount: RequestBody,
        @Part("currentAmount") currentAmount: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part file: MultipartBody.Part?, // bisa null
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @GET("targets")
    fun getTarget(
        @Header("Authorization") token:String
    ): Call<TargetResponse>

    //Transaction
    @FormUrlEncoded
    @POST("transaksi")
    fun createTransaction(
        @Field("categories_id") categoryFieldString: String,
        @Field("jenis") jenisString: String,
        @Field("descripsi") descString: String,
        @Field("jumlah") jumlahString: String,
        @Field("date") dateString: String,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @GET("transaksi/index")
    fun indexTransaction(
        @Header("Authorization") token: String
    ): Call<TransactionResponse>


}