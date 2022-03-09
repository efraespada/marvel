package com.efraespada.data.interfaces

import com.efraespada.data.model.HeroesResponse

interface HeroApi {
    suspend fun getHeroes(
        offset: Int,
        limit: Int,
        apiKey: String,
        privateKey: String,
    ): HeroesResponse

    suspend fun getHeroDetail(
        id: String,
        apiKey: String,
        privateKey: String,
    ): HeroesResponse
}