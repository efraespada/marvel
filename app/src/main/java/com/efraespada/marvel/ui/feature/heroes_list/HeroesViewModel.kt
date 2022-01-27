package com.efraespada.marvel.ui.feature.heroes_list

import androidx.lifecycle.viewModelScope
import com.efraespada.marvel.base.BaseViewModel
import com.efraespada.marvel.model.data.HeroRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroesViewModel @Inject constructor(private val repository: HeroRepository) :
    BaseViewModel<HeroesContract.Event, HeroesContract.State, HeroesContract.Effect>() {

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
        }
    }

    private suspend fun getHeroes() {
        val heroes = repository.getShortHeroes()
        setState {
            copy(heroes = heroes, isLoading = false)
        }
        setEffect { HeroesContract.Effect.DataWasLoaded }
    }

}
