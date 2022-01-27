package com.efraespada.marvel.ui.feature.heroes_list

import com.efraespada.marvel.base.ViewEvent
import com.efraespada.marvel.base.ViewSideEffect
import com.efraespada.marvel.base.ViewState
import com.efraespada.marvel.model.response.Hero

class HeroesContract {
    sealed class Event : ViewEvent {
        data class HeroSelection(val heroId: String) : Event()
    }

    data class State(val heroes: List<Hero> = listOf(), val isLoading: Boolean = false) :
        ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToHeroDetails(val heroId: String) : Navigation()
        }
    }

}