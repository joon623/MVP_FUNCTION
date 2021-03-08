package com.sleep.aplusasleep

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AsleepApplication : Application() {


    // 임시 API
    val API_URL = "http://api.openweathermap.org/data/2.5/";

    companion object {
        var TAG = "TEMPLATE_APP"

        // 전역 SP 인스턴스
        lateinit var sSharedPreferences: SharedPreferences

        // 전역 레트로핏 인스턴스
        lateinit var sRetrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        sSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        initRetrofitInstance()
    }

    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .build()

        sRetrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}