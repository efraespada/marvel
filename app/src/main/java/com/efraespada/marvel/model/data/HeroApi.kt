package com.efraespada.marvel.model.data

import com.efraespada.marvel.model.credentials.CredentialsProvider
import com.efraespada.marvel.model.credentials.CredentialsProviderImpl
import com.efraespada.marvel.model.response.HeroesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroApi @Inject constructor(
    private val service: Service,
) {
    var credentialProvider: CredentialsProvider = CredentialsProviderImpl()

    suspend fun getHeroes(): HeroesResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroes(
            ts,
            credentialProvider.getApiKey(),
            md5("$ts${credentialProvider.getPrivateKey()}${credentialProvider.getApiKey()}")
        )
    }

    suspend fun getHeroDetail(id: String): HeroesResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroDetail(
            id,
            ts,
            credentialProvider.getApiKey(),
            md5("$ts${credentialProvider.getPrivateKey()}${credentialProvider.getApiKey()}")
        )
    }

    interface Service {
        @GET("/v1/public/characters")
        suspend fun getHeroes(
            @Query("ts") ts: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
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
