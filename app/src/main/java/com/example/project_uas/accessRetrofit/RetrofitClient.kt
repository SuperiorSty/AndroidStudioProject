package com.example.project_uas.accessRetrofit

import android.util.Log
import com.example.project_uas.modelData.ApiService
import com.example.project_uas.modelData.Obat
import com.example.project_uas.modelData.Pengingat
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

//    private const val BASE_URL = "http://172.27.193.176:4000/"
    private const val BASE_URL = "https://uas.stymc.my.id"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val responseBody = response.body
            val raw = responseBody?.string()

            Log.d("RAW_HTTP", raw ?: "null")

            response.newBuilder()
                .body(raw?.toResponseBody(responseBody?.contentType()))
                .build()
        }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    interface RetrofitCallback {
        fun onObatLoaded(data: List<Obat>)
        fun onPengingatLoaded(data: List<Pengingat>)
    }
}
