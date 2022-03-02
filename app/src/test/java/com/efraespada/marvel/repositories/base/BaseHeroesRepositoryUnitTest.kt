package com.efraespada.marvel.repositories.base

import com.efraespada.marvel.repositories.builders.heroRepository

open class BaseHeroesRepositoryUnitTest : BaseUnitTest() {
    val repository = heroRepository()
}
