package com.efraespada.domain.usecase

import com.efraespada.data.data.HeroRepositoryImpl
import com.efraespada.domain.extenstions.toList
import com.efraespada.domain.model.Hero
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class GetHeroesUseCase @Inject constructor(
    private val heroRepository: HeroRepositoryImpl,
) {
    suspend operator fun invoke(
        offset: Int,
        apiKey: String,
        privateKey: String,
    ): List<Hero> = heroRepository.getHeroesList(offset, apiKey, privateKey).toList()
}
