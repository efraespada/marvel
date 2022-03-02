package com.efraespada.marvel.model.data

import com.efraespada.marvel.model.response.Hero
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(private val heroApi: HeroApi) {

    suspend fun getHeroesList() = try {
        heroApi.getHeroes().data.results
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
