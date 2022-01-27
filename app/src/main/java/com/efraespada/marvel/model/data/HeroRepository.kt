package com.efraespada.marvel.model.data


import com.efraespada.marvel.model.response.Hero
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(private val heroApi: HeroApi) {

    /**
     * Do any DTO transformation here
     */
    suspend fun getShortHeroes() = try {
        heroApi.getHeroes().data.results
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getHero(id: String): Hero {
        return heroApi.getHeroDetail(id).data.results.first()
    }

}