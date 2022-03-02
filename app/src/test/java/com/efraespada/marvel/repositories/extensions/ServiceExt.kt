package com.efraespada.marvel.repositories.extensions

import kotlin.reflect.KClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <T : Any> KClass<T>.service(url: String): T {
    return Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(this.java) as T
}
