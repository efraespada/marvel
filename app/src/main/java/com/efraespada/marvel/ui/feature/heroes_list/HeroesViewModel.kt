package com.efraespada.marvel.ui.feature.heroes_list

import androidx.lifecycle.viewModelScope
import com.efraespada.domain.model.Hero
import com.efraespada.domain.usecase.GetHeroesUseCase
import com.efraespada.marvel.base.BaseViewModel
import com.efraespada.marvel.model.credentials.CredentialsProviderImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HeroesViewModel @Inject constructor(
    private val getHeroesUseCase: GetHeroesUseCase,
) : BaseViewModel<HeroesContract.Event, HeroesContract.State, HeroesContract.Effect>() {

    override val credentialsProvider = CredentialsProviderImpl()

    private val initialIndex = 0

    private var moreData = true
    private var offset = 0

    init {
        viewModelScope.launch { getHeroes() }
    }

    override fun setInitialState() =
        HeroesContract.State(heroes = listOf(), isLoading = true)

    override fun handleEvents(event: HeroesContract.Event) {
        when (event) {
            is HeroesContract.Event.HeroSelection -> {
                setEffect { HeroesContract.Effect.Navigation.ToHeroDetails(event.heroId) }
            }
            is HeroesContract.Event.ListAtEnt -> {
                viewModelScope.launch { getHeroes() }
            }
        }
    }

    private suspend fun getHeroes() {
        if (!moreData) return

        if (offset != initialIndex) {
            setEffect { HeroesContract.Effect.LoadingMoreData }
        }

        val heroes = getHeroesUseCase(
            offset,
            credentialsProvider.getApiKey(),
            credentialsProvider.getPrivateKey(),
        )
        moreData = heroes.isNotEmpty()

        if (!moreData) {
            setEffect { HeroesContract.Effect.NoMoreDataToShow }
            return
        }

        setState {
            val list = mutableListOf<Hero>()
            list.addAll(this.heroes)
            list.addAll(heroes)
            copy(heroes = list, isLoading = false)
        }

        if (offset == initialIndex) {
            setEffect { HeroesContract.Effect.DataWasLoaded }
        }

        offset += heroes.size
    }
}
