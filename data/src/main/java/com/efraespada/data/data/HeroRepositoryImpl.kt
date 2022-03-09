package com.efraespada.data.data

import com.efraespada.data.interfaces.HeroRepository
import com.efraespada.data.model.HeroModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class HeroRepositoryImpl @Inject constructor(
    private val heroApi: HeroApiImpl,
) : HeroRepository {

    private val initialOffset = 0
    private val limit = 100

    override suspend fun getHeroesList(
        offset: Int?,
        apiKey: String,
        privateKey: String,
    ) = try {
        heroApi.getHeroes(offset ?: initialOffset, limit, apiKey, privateKey).data.results
    } catch (e: Exception) {
        emptyList()
    }

    override suspend fun getHero(
        id: String,
        apiKey: String,
        privateKey: String,
    ): HeroModel? {
        val results = heroApi.getHeroDetail(id, apiKey, privateKey).data.results
        return when {
            results.isEmpty() -> null
            else -> results.first()
        }
    }
}
