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
import com.efraespada.marvel.model.response.Comic
import com.efraespada.marvel.model.response.Hero
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
            Surface(elevation = 4.dp) {
                HeroDetailCollapsingToolbar(state.hero, scrollOffset)
            }
            Spacer(modifier = Modifier.height(2.dp))
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
                    modifier = Modifier.padding(horizontal = 12.dp),
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
    val imageSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
    Column {
        Row {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
                border = BorderStroke(
                    width = 2.dp,
                    color = Color.Black
                ),
                elevation = 4.dp
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "${hero?.thumbnail?.path}.${hero?.thumbnail?.extension}".replace(
                            "http:",
                            "https:"
                        ),
                        builder = {
                            transformations(CircleCropTransformation())
                        },
                    ),
                    modifier = Modifier.size(max(72.dp, imageSize)),
                    contentDescription = R.string.contentDescriptionHero.reveal(),
                )
            }
            HeroDetailItem(
                item = hero,
                expandedLines = (kotlin.math.max(3f, scrollOffset * 6)).toInt(),
                modifier = Modifier
                    .padding(
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
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
        contentPadding = PaddingValues(bottom = 16.dp)
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
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .testTag(tagComicItem)
    ) {
        Row(modifier = Modifier.animateContentSize()) {
            ComicItemDetails(
                item = item,
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 24.dp,
                        bottom = 24.dp
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
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
