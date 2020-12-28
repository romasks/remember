package com.remember.app.data.dataFlow.network

import com.google.gson.Gson
import com.remember.app.data.Constants.BASE_SERVICE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val timeout: Long = 30

object ApiService {

    private val requestInterceptor = RequestInterceptor()
    private val httpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(createLogger())
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)
            .followRedirects(true).build()

    private fun createLogger(): Interceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    fun getApi(): Api = Retrofit.Builder()
            .baseUrl(BASE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(httpClient)
            .build()
            .create(Api::class.java)
}