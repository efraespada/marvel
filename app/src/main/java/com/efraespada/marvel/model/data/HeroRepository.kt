package com.efraespada.marvel.model.data

import com.efraespada.marvel.model.response.Hero
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(private val heroApi: HeroApi) {
    private val initialOffset = 0
    private val limit = 100

    suspend fun getHeroesList(offset: Int = initialOffset) = try {
        heroApi.getHeroes(offset, limit).data.results
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getHero(id: String): Hero? {
        val results = heroApi.getHeroDetail(id).data.results
        return when {
            results.isEmpty() -> null
            else -> results.first()
        }
    }
}
