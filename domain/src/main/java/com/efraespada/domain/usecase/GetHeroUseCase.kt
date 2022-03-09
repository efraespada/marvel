package com.efraespada.domain.usecase

import com.efraespada.data.data.HeroRepositoryImpl
import com.efraespada.domain.extenstions.to
import com.efraespada.domain.model.Hero
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class GetHeroUseCase @Inject constructor(
    private val heroRepository: HeroRepositoryImpl,
) {
    suspend operator fun invoke(
        id: String,
        apiKey: String,
        privateKey: String,
    ): Hero = heroRepository.getHero(id, apiKey, privateKey).to()
}