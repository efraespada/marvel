package com.efraespada.marvel.ui.feature.hero_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.efraespada.marvel.base.BaseViewModel
import com.efraespada.marvel.model.data.HeroRepository
import com.efraespada.marvel.ui.feature.entry.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: HeroRepository
) : BaseViewModel<
    HeroDetailContract.Event,
    HeroDetailContract.State,
    HeroDetailContract.Effect>() {

    init {
        viewModelScope.launch {
            val heroId = stateHandle.get<String>(NavigationKeys.Arg.HERO_ID)
                ?: throw IllegalStateException("No hero id was passed to destination.")
            val heroDetail = repository.getHero(heroId)
            setState { copy(hero = heroDetail) }
        }
    }

    override fun setInitialState() = HeroDetailContract.State(null)

    override fun handleEvents(event: HeroDetailContract.Event) {}
}
