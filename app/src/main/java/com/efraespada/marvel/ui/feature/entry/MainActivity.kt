package com.efraespada.marvel.ui.feature.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.efraespada.marvel.ui.feature.entry.NavigationKeys.Arg.HERO_ID
import com.efraespada.marvel.ui.feature.hero_detail.HeroDetailScreen
import com.efraespada.marvel.ui.feature.hero_detail.HeroDetailViewModel
import com.efraespada.marvel.ui.feature.heroes_list.HeroesContract
import com.efraespada.marvel.ui.feature.heroes_list.HeroesScreen
import com.efraespada.marvel.ui.feature.heroes_list.HeroesViewModel
import com.efraespada.marvel.ui.theme.MarvelTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelTheme {
                MarvelApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarvelTheme {
        Greeting("Android")
    }
}

@ExperimentalCoilApi
@Composable
private fun MarvelApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.HERO_LIST) {
        composable(route = NavigationKeys.Route.HERO_LIST) {
            HeroesScreenComposable(navController)
        }
        composable(
            route = NavigationKeys.Route.HERO_DETAILS,
            arguments = listOf(
                navArgument(HERO_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            HeroDetailScreenComposable()
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun HeroesScreenComposable(navController: NavHostController) {
    val viewModel: HeroesViewModel = hiltViewModel()
    val state = viewModel.viewState.value
    HeroesScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            if (navigationEffect is HeroesContract.Effect.Navigation.ToHeroDetails) {
                navController.navigate(
                    "${NavigationKeys.Route.HERO_LIST}/${navigationEffect.heroId}"
                )
            }
        }
    )
}

@ExperimentalCoilApi
@Composable
private fun HeroDetailScreenComposable() {
    val viewModel: HeroDetailViewModel = hiltViewModel()
    val state = viewModel.viewState.value
    HeroDetailScreen(state)
}

object NavigationKeys {

    object Arg {
        const val HERO_ID = "heroId"
    }

    object Route {
        const val HERO_LIST = "heroes_list"
        const val HERO_DETAILS = "$HERO_LIST/{$HERO_ID}"
    }
}
