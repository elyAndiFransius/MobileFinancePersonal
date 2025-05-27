package com.example.personalfinancemobile.app.data.network

import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServices {
    @POST("register")
    fun registerUser(@Body user: User): Call<User>

    @POST("budgets")
    fun createBudget(@Body budgets: List<Budget> ): Call<List<Budget>>

    @POST("budgets")
    fun createBudgetRequest(@Body budget: BudgetRequest): Call<ResponseBody>
}