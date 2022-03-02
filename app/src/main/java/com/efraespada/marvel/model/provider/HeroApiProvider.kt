package com.efraespada.marvel.model.provider

import com.efraespada.marvel.model.data.HeroApi
import com.efraespada.marvel.model.data.HeroApi.Companion.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class HeroApiProvider {

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideHeroApiService(
        retrofit: Retrofit
    ): HeroApi.Service {
        return retrofit.create(HeroApi.Service::class.java)
    }
}
