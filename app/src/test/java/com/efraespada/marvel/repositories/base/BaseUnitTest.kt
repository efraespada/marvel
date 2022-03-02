package com.efraespada.marvel.repositories.base

import com.efraespada.marvel.repositories.rules.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

open class BaseUnitTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = CoroutineRule()
}
