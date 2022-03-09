package com.efraespada.data.interfaces

import com.efraespada.data.model.HeroModel

interface HeroRepository {
    suspend fun getHeroesList(
        offset: Int?,
        apiKey: String,
        privateKey: String,
    ): List<HeroModel>

    suspend fun getHero(
        id: String,
        apiKey: String,
        privateKey: String,
    ): HeroModel?
}
