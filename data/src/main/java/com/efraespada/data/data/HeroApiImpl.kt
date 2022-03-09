package com.efraespada.data.data

import com.efraespada.data.model.HeroesResponse
import com.efraespada.data.interfaces.HeroApi
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class HeroApiImpl @Inject constructor(
    private val service: Service,
) : HeroApi {

    override suspend fun getHeroes(
        offset: Int, limit: Int,
        apiKey: String,
        privateKey: String,
    ): HeroesResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroes(
            ts,
            apiKey,
            md5("$ts${privateKey}${apiKey}"),
            offset,
            limit
        )
    }

    override suspend fun getHeroDetail(
        id: String,
        apiKey: String,
        privateKey: String,
    ): HeroesResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroDetail(
            id,
            ts,
            apiKey,
            md5("$ts${privateKey}${apiKey}")
        )
    }

    interface Service {
        @GET("/v1/public/characters")
        suspend fun getHeroes(
            @Query("ts") ts: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int,
        ): HeroesResponse

        @GET("/v1/public/characters/{id}")
        suspend fun getHeroDetail(
            @Path("id") id: String,
            @Query("ts") ts: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
        ): HeroesResponse
    }

    companion object {
        const val API_URL = "https://gateway.marvel.com"
    }

    private fun md5(value: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(value.toByteArray()))
            .toString(16).padStart(32, '0')
    }
}
