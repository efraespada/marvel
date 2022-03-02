package com.efraespada.marvel.model.data

import com.efraespada.marvel.md5
import com.efraespada.marvel.model.credentials.CredentialsProvider
import com.efraespada.marvel.model.credentials.CredentialsProviderImpl
import com.efraespada.marvel.model.response.HeroDetailResponse
import com.efraespada.marvel.model.response.ShortHeroResponse
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Singleton
class HeroApi @Inject constructor(
    private val service: Service,
) {
    var credentialProvider: CredentialsProvider = CredentialsProviderImpl()

    suspend fun getHeroes(): ShortHeroResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroes(
            ts,
            credentialProvider.getApiKey(),
            "$ts${credentialProvider.getPrivateKey()}${credentialProvider.getApiKey()}".md5()
        )
    }

    suspend fun getHeroDetail(id: String): HeroDetailResponse {
        val ts = System.currentTimeMillis().toString()
        return service.getHeroDetail(
            id,
            ts,
            credentialProvider.getApiKey(),
            "$ts${credentialProvider.getPrivateKey()}${credentialProvider.getApiKey()}".md5()
        )
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
