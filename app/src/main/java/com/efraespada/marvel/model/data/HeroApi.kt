package com.efraespada.marvel.model.data

import com.efraespada.marvel.R
import com.efraespada.marvel.md5
import com.efraespada.marvel.model.response.HeroDetailResponse
import com.efraespada.marvel.model.response.ShortHeroResponse
import com.stringcare.library.reveal
import com.stringcare.library.string
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroApi @Inject constructor(private val service: Service) {

    suspend fun getHeroes(): ShortHeroResponse {
        val ts = System.currentTimeMillis().toString()
        val apiKey = R.string.apiKey.reveal()
        val privateKey = R.string.privateKey.reveal()
        return service.getHeroes(ts, apiKey, "$ts$privateKey$apiKey".md5())
    }

    suspend fun getHeroDetail(id: String): HeroDetailResponse {
        val ts = System.currentTimeMillis().toString()
        val apiKey = R.string.apiKey.reveal()
        val privateKey = R.string.privateKey.reveal()
        return service.getHeroDetail(id, ts, apiKey, "$ts$privateKey$apiKey".md5())
    }

    interface Service {
        @GET("/v1/public/characters")
        suspend fun getHeroes(
            @Query("ts") ts: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
        ): ShortHeroResponse

        @GET("/v1/public/characters/{id}")
        suspend fun getHeroDetail(
            @Path("id") id: String,
            @Query("ts") ts: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
        ): HeroDetailResponse
    }

    companion object {
        const val API_URL = "https://gateway.marvel.com"
    }
}


