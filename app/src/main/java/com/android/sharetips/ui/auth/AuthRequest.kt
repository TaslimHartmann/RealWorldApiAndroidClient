package com.android.sharetips.ui.auth

import com.android.sharetips.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRequest {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://conduit.productionready.io/api/"
//    private const val BASE_URL = "http://10.0.2.2/api/"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                val builder = OkHttpClient.Builder()
                builder.retryOnConnectionFailure(false)
                builder.interceptors().add(HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                val client = builder.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit
        }
}