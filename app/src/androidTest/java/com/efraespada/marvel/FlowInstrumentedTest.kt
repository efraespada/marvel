package com.efraespada.marvel

import androidx.compose.ui.test.*
import coil.annotation.ExperimentalCoilApi
import com.efraespada.marvel.base.*
import com.efraespada.marvel.ui.feature.entry.DefaultPreview
import com.stringcare.library.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@ExperimentalCoilApi
@HiltAndroidTest
class FlowInstrumentedTest : BaseInstrumentedTest() {
    private val delay = 3000L

    @Test
    fun appLaunch() {
        composeTestRule.setContent { DefaultPreview() }
        composeTestRule.onNodeWithText(R.string.app_name.string()).assertIsDisplayed()
    }

    @Test
    fun appLaunchAndPicksAHero() {
        composeTestRule.setContent { DefaultPreview() }
        composeTestRule.onNodeWithText(R.string.app_name.string()).assertIsDisplayed()
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagHeroItem).onFirst().assertIsDisplayed()
    }

    @Test
    fun appOpenHeroDetail() {
        composeTestRule.setContent { DefaultPreview() }
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagHeroItem).onFirst().performClick()
        asyncTimer(delay)
        composeTestRule.onNodeWithTag(tagHeroDetailTabBar).assertIsDisplayed()
    }

    @Test
    fun appOpenHeroDetailWithComics() {
        composeTestRule.setContent { DefaultPreview() }
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagHeroItem).onFirst().performClick()
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagComicItem).onFirst().assertIsDisplayed()
    }

    @Test
    fun appOpenHeroDetailWithComicsAndClosesDetails() {
        composeTestRule.setContent { DefaultPreview() }
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagHeroItem).onFirst().performClick()
        asyncTimer(delay)
        composeTestRule.onAllNodesWithTag(testTag = tagComicItem).onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithTag(tagBack).assertIsDisplayed()
        composeTestRule.onNodeWithTag(tagBack).performClick()
        asyncTimer(delay)
        composeTestRule.onNodeWithText(R.string.app_name.string()).assertIsDisplayed()
    }
}
