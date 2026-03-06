package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

// Оставляем вашу модель Country
interface ApiService {
    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,capital,region,population"
    ): List<Country>
}

object RetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/"

    val api: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}