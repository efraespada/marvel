package com.efraespada.marvel

import coil.annotation.ExperimentalCoilApi
import com.efraespada.marvel.base.BaseInstrumentedTest
import com.stringcare.library.obfuscate
import com.stringcare.library.reveal
import com.stringcare.library.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@ExperimentalCoilApi
@HiltAndroidTest
class SecurityInstrumentedTest : BaseInstrumentedTest() {

    /**
     * Checks if stringcare was loaded with application context.
     */
    @Test
    fun scInitialized() = try {
        R.string.app_name.string().obfuscate()
        assert(true)
    } catch (e: Exception) {
        assert(false)
    }

    @Test
    fun revealApiKey() {
        val original = R.string.apiKey.string()
        assert(original.isNotEmpty())
        val revealed = R.string.apiKey.reveal()
        assert(revealed.isNotEmpty())
        assert(revealed != original)
    }

    @Test
    fun revealPrivateKey() {
        val original = R.string.privateKey.string()
        assert(original.isNotEmpty())
        val revealed = R.string.privateKey.reveal()
        assert(revealed.isNotEmpty())
        assert(revealed != original)
    }
}
