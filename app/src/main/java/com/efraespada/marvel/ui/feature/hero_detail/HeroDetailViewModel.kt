package com.efraespada.marvel.ui.feature.hero_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.efraespada.domain.usecase.GetHeroUseCase
import com.efraespada.marvel.base.BaseViewModel
import com.efraespada.marvel.model.credentials.CredentialsProviderImpl
import com.efraespada.marvel.ui.feature.entry.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val getHeroUseCase: GetHeroUseCase,
) : BaseViewModel<
    HeroDetailContract.Event,
    HeroDetailContract.State,
    HeroDetailContract.Effect>() {

    override val credentialsProvider = CredentialsProviderImpl()

    init {
        viewModelScope.launch {
            val heroId = stateHandle.get<String>(NavigationKeys.Arg.HERO_ID)
                ?: throw IllegalStateException("No hero id was passed to destination.")
            val heroDetail = getHeroUseCase(
                heroId,
                credentialsProvider.getApiKey(),
                credentialsProvider.getPrivateKey(),
            )
            setState { copy(hero = heroDetail) }
        }
    }

    override fun setInitialState() = HeroDetailContract.State(null)

    override fun handleEvents(event: HeroDetailContract.Event) {
        // nothing to do here
    }
}
