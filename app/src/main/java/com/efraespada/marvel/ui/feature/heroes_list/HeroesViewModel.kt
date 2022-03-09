package com.efraespada.marvel.ui.feature.heroes_list

import androidx.lifecycle.viewModelScope
import com.efraespada.marvel.base.BaseViewModel
import com.efraespada.marvel.model.data.HeroRepository
import com.efraespada.marvel.model.response.Hero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HeroesViewModel @Inject constructor(private val repository: HeroRepository) :
    BaseViewModel<HeroesContract.Event, HeroesContract.State, HeroesContract.Effect>() {

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

        val heroes = repository.getHeroesList(offset)
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
