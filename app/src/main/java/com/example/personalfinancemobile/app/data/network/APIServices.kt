package com.example.personalfinancemobile.app.data.network

import android.telecom.CallScreeningService.CallResponse
import com.example.personalfinancemobile.app.data.model.Auth.GrafikCategori
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.model.Auth.loginRequest
import com.example.personalfinancemobile.app.data.model.Auth.loginResponse
import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.CategoryResponse
import com.example.personalfinancemobile.app.data.model.DepositResponse
import com.example.personalfinancemobile.app.data.model.ServerCategory
import com.example.personalfinancemobile.app.data.model.Target
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.model.TransactionResponse
import com.example.personalfinancemobile.app.data.model.UserResponseObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.personalfinancemobile.app.data.model.Target as TargetModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface APIServices {

    // Authentifikasi
    @POST("register")
    fun registerUser(@Body user: User): Call<User>

    @POST("login")
    fun login(@Body request: loginRequest): Call<loginResponse>

    @GET("profile")
    fun getUser(
        @Header("Authorization") token: String
    ): Call<UserResponseObject>

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


    @PUT("budgets/update/{budget}")
    fun updateBudgeting(
        @Path("budget") id: Int,
        @Body budget: BudgetRequest,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @DELETE("budgets/delete/{budget}")
    fun deleteBudgeting(
        @Path("budget") id: Int,
        @Header("Authorization") token: String
    ): Call<ResponseBody>


    @GET("targets")
    fun getTarget(
        @Header("Authorization") token:String
    ): Call<TargetResponse>

    // Target
    @DELETE("targets/destory/{target}")
    fun deleteTarget(
        @Path("target") id: Int,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @Multipart
    @POST("targets/store")
    fun createTarget(
        @Part("gol") gol: RequestBody,
        @Part("targetAmount") targetAmount: RequestBody,
        @Part("currentAmount") currentAmount: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part file: MultipartBody.Part?, // bisa null
        @Header("Authorization") token: String
    ): Call<ResponseBody>


    @GET("deposit")
    fun indexDeposit(
        @Header("Authorization") token : String
    ):  Call<DepositResponse>

    // Deposit
    @Multipart
    @POST("deposit/store")
    fun createDeposit(
        @Part ("date") date: RequestBody,
        @Part("deposit") deposit: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @PUT("deposit/update/{deposit}")
    fun updateDeposit(
        @Path("deposit") id: Int,
        @Field("date") date: String,
        @Field("deposit") deposit: Int,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @DELETE("deposit/delete/{deposit}")
    fun deletedepo(
        @Path("deposit") id: Int,
        @Header("Authorization") token: String
    ): Call<ResponseBody>


    //Transaction
    @FormUrlEncoded
    @POST("transaksi/store")
    fun createTransaction(
        @Field("categories_id") categoryFieldString: String,
        @Field("jenis") jenisString: String,
        @Field("descripsi") descString: String,
        @Field("jumlah") jumlahString: String,
        @Field("date") dateString: String,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    //Transaction
    @FormUrlEncoded
    @PUT("transaksi/update/{transaksi}")
    fun updateTransaction(
        @Path("transaksi") id: Int,
        @Field("categories_id") categoryFieldString: String,
        @Field("jenis") jenisString: String,
        @Field("descripsi") descString: String,
        @Field("jumlah") jumlahString: String,
        @Field("date") dateString: String,
        @Header("Authorization") token: String
    ): Call<ResponseBody>


    @DELETE("transaksi/delete/{transaksi}")
    fun deleteTransaction(
        @Path("transaksi") id: Int,
        @Header("Authorization") token: String
    ): Call<ResponseBody>


    @GET("transaksi")
    fun indexTransaction(
        @Header("Authorization") token: String
    ): Call<TransactionResponse>

    // untuk menampilkan kategories saja
    @GET("categories")
    fun getCategories(
        @Header("Authorization") token : String
    ):  Call<CategoryResponse>

    // untuk menampilkak grafi
    @GET("grafik")
    fun getKategoriData(
        @Header("Authorization") token : String
    ):  Call<List<GrafikCategori>>


}