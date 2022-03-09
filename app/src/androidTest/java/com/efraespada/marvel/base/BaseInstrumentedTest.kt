package com.efraespada.marvel.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import coil.annotation.ExperimentalCoilApi
import com.efraespada.marvel.BuildConfig
import com.efraespada.marvel.ui.feature.entry.MainActivity
import com.stringcare.library.SC
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.util.*
import kotlin.concurrent.schedule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoilApi
@HiltAndroidTest
open class BaseInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        SC.init { appContext }
    }

    /**
     * This is a default test when creating an instrumented test but
     * it is a sensitive value when working with Stringcare.
     */
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals(BuildConfig.APPLICATION_ID, appContext.packageName)
    }

    fun asyncTimer(delay: Long = 1000) {
        AsyncTimer.start(delay)
        composeTestRule.waitUntil(
            condition = { AsyncTimer.expired },
            timeoutMillis = delay + 1000
        )
    }

    object AsyncTimer {
        var expired = false
        fun start(delay: Long = 1000) {
            expired = false
            Timer().schedule(delay) {
                expired = true
            }
        }
    }
}
