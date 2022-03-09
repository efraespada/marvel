package com.efraespada.marvel.ui.feature.heroes_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.efraespada.marvel.R
import com.efraespada.marvel.base.LAUNCH_LISTEN_FOR_EFFECTS
import com.efraespada.marvel.base.tagHeroItem
import com.efraespada.marvel.int
import com.efraespada.marvel.model.response.Hero
import com.efraespada.marvel.noRippleClickable
import com.efraespada.marvel.safe
import com.efraespada.marvel.ui.theme.MarvelTheme
import com.stringcare.library.reveal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalCoilApi
@Composable
fun HeroesScreen(
    state: HeroesContract.State,
    effectFlow: Flow<HeroesContract.Effect>?,
    onEventSent: (event: HeroesContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: HeroesContract.Effect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HeroesContract.Effect.DataWasLoaded ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = R.string.snackHeroesLoaded.reveal(),
                        duration = SnackbarDuration.Short
                    )
                is HeroesContract.Effect.Navigation.ToHeroDetails -> onNavigationRequested(
                    effect
                )
                is HeroesContract.Effect.LoadingMoreData ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = R.string.snackLoadingMoreHeroes.reveal(),
                        duration = SnackbarDuration.Short
                    )
                is HeroesContract.Effect.NoMoreDataToShow ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = R.string.snackNoMoreHeroes.reveal(),
                        duration = SnackbarDuration.Short
                    )
            }
        }?.collect()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HeroesAppBar()
        },
    ) {
        Box {
            HeroesList(
                heroItems = state.heroes,
                onItemClicked = { itemId ->
                    onEventSent(HeroesContract.Event.HeroSelection(itemId))
                },
                endOfList = {
                    onEventSent(HeroesContract.Event.ListAtEnt)
                }
            )
            if (state.isLoading)
                LoadingBar()
        }
    }
}

@Composable
private fun HeroesAppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                modifier = Modifier.padding(horizontal = R.integer.default_padding.int().dp),
                contentDescription = R.string.contentDescriptionAction.reveal()
            )
        },
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.background
    )
}

@ExperimentalCoilApi
@Composable
fun HeroesList(
    heroItems: List<Hero>,
    onItemClicked: (id: String) -> Unit = { },
    endOfList: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(bottom = R.integer.default_padding.int().dp),
        state = listState
    ) {
        items(heroItems) { item ->
            if (heroItems.last() == item) {
                endOfList()
            }
            HeroItemRow(
                item = item,
                itemShouldExpand = item.description.isNotEmpty(),
                onItemClicked = onItemClicked,
                iconTransformationBuilder = {
                    this.error(R.drawable.ic_launcher_background)
                },
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HeroItemRow(
    item: Hero,
    itemShouldExpand: Boolean = false,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit = { },
    onItemClicked: (id: String) -> Unit = { }
) {
    Card(
        shape = RoundedCornerShape(R.integer.shape_round.int().dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = R.integer.default_elevation.int().dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = R.integer.default_padding.int().dp,
                end = R.integer.default_padding.int().dp,
                top = R.integer.default_padding.int().dp
            )
            .clickable { onItemClicked(item.id) }
            .testTag(tagHeroItem)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Row(modifier = Modifier.animateContentSize()) {
            Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                HeroItemThumbnail(
                    "${item.thumbnail.path}.${item.thumbnail.extension}",
                    iconTransformationBuilder
                )
            }
            HeroItemDetails(
                item = item,
                expandedLines = when {
                    expanded -> R.integer.lines_expanded.int()
                    else -> R.integer.lines_not_expanded.int()
                },
                modifier = Modifier
                    .padding(
                        start = R.integer.item_padding_horizontal.int().dp,
                        end = R.integer.item_padding_horizontal.int().dp,
                        top = R.integer.item_padding_vertical.int().dp,
                        bottom = R.integer.item_padding_vertical.int().dp
                    )
                    .fillMaxWidth(0.80f)
                    .align(Alignment.CenterVertically)
            )
            if (itemShouldExpand)
                Box(
                    modifier = Modifier
                        .align(if (expanded) Alignment.Bottom else Alignment.CenterVertically)
                        .noRippleClickable { expanded = !expanded }
                ) {
                    ExpandableContentIcon(expanded)
                }
        }
    }
}

@Composable
private fun ExpandableContentIcon(expanded: Boolean) {
    Icon(
        imageVector = when {
            expanded -> Icons.Filled.KeyboardArrowUp
            else -> Icons.Filled.KeyboardArrowDown
        },
        contentDescription = R.string.contentDescriptionExpand.reveal(),
        modifier = Modifier
            .padding(all = R.integer.default_padding.int().dp)
    )
}

@Composable
fun HeroItemDetails(
    item: Hero?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item?.name ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            maxLines = R.integer.lines_name.int(),
            overflow = TextOverflow.Ellipsis
        )
        if (item?.description?.trim()?.isNotEmpty() == true)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.description.trim(),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = expandedLines
                )
            }
    }
}

@ExperimentalCoilApi
@Composable
fun HeroItemThumbnail(
    thumbnailUrl: String,
    iconTransformationBuilder: ImageRequest.Builder.() -> Unit
) {
    Image(
        painter = rememberImagePainter(
            data = thumbnailUrl.safe(),
            builder = iconTransformationBuilder
        ),
        modifier = Modifier
            .size(R.integer.hero_profile_size.int().dp)
            .padding(
                start = R.integer.default_padding.int().dp,
                top = R.integer.default_padding.int().dp,
                bottom = R.integer.default_padding.int().dp
            ),
        contentDescription = R.string.contentDescriptionHero.reveal(),
    )
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarvelTheme {
        HeroesScreen(HeroesContract.State(), null, { }, { })
    }
}
