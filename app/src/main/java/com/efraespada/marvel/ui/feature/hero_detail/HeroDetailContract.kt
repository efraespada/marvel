package com.efraespada.marvel.ui.feature.hero_detail

import com.efraespada.marvel.base.ViewEvent
import com.efraespada.marvel.base.ViewSideEffect
import com.efraespada.marvel.base.ViewState
import com.efraespada.marvel.model.response.Hero

class HeroDetailContract {
    sealed class Event : ViewEvent

    data class State(
        val hero: Hero?,
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
