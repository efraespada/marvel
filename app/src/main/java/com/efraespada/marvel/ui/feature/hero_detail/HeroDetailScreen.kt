package com.efraespada.marvel.ui.feature.hero_detail

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.efraespada.marvel.R
import com.efraespada.marvel.base.tagBack
import com.efraespada.marvel.base.tagComicItem
import com.efraespada.marvel.base.tagHeroDetailTabBar
import com.efraespada.marvel.int
import com.efraespada.marvel.model.response.Comic
import com.efraespada.marvel.model.response.Hero
import com.efraespada.marvel.safe
import com.stringcare.library.reveal
import kotlin.math.min

@ExperimentalCoilApi
@Composable
fun HeroDetailScreen(state: HeroDetailContract.State) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HeroAppBar(state.hero)
        },
    ) {
        Column {
            Surface(elevation = R.integer.default_elevation.int().dp) {
                HeroDetailCollapsingToolbar(state.hero, scrollOffset)
            }
            Spacer(modifier = Modifier.height(R.integer.default_stroke_width.int().dp))
            ComicsList(comicItems = state.hero?.comics?.items ?: emptyList())
        }
    }
}

@Composable
private fun HeroAppBar(
    item: Hero?,
) {
    val activity = LocalContext.current as Activity
    val navController = rememberNavController()
    TopAppBar(
        modifier = Modifier.testTag(tagHeroDetailTabBar),
        navigationIcon = {
            IconButton(
                modifier = Modifier.testTag(tagBack),
                onClick = {
                    if (!navController.popBackStack()) activity.onBackPressed()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier.padding(horizontal = R.integer.padding_icon.int().dp),
                    contentDescription = tagBack
                )
            }
        },
        title = { Text(item?.name ?: "") },
        backgroundColor = MaterialTheme.colors.background,

    )
}

@Composable
fun HeroDetailItem(
    item: Hero?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
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
private fun HeroDetailCollapsingToolbar(
    hero: Hero?,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(
        targetValue = max(
            R.integer.minHeaderHeight.int().dp,
            R.integer.maxHeaderHeight.int().dp * scrollOffset
        )
    )
    Column {
        Row {
            Card(
                modifier = Modifier.padding(R.integer.default_padding.int().dp),
                shape = CircleShape,
                border = BorderStroke(
                    width = R.integer.default_stroke_width.int().dp,
                    color = Color.Black
                ),
                elevation = R.integer.default_elevation.int().dp
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "${hero?.thumbnail?.path}.${hero?.thumbnail?.extension}".safe(),
                        builder = {
                            transformations(CircleCropTransformation())
                        },
                    ),
                    modifier = Modifier.size(max(R.integer.hero_profile_size.int().dp, imageSize)),
                    contentDescription = R.string.contentDescriptionHero.reveal(),
                )
            }
            HeroDetailItem(
                item = hero,
                expandedLines = (
                    kotlin.math.max(
                        3f,
                        scrollOffset * R.integer.lines_expanded_description.int()
                    )
                    ).toInt(),
                modifier = Modifier
                    .padding(
                        end = R.integer.default_padding.int().dp,
                        top = R.integer.default_padding.int().dp,
                        bottom = R.integer.default_padding.int().dp
                    )
                    .fillMaxWidth()
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ComicsList(
    comicItems: List<Comic>,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = R.integer.default_padding.int().dp)
    ) {
        items(comicItems) { item ->
            ComicItemRow(
                item = item,
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ComicItemRow(
    item: Comic,
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
            .testTag(tagComicItem)
    ) {
        Row(modifier = Modifier.animateContentSize()) {
            ComicItemDetails(
                item = item,
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
        }
    }
}

@Composable
fun ComicItemDetails(
    item: Comic?,
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
    }
}
