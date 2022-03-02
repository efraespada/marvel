package com.efraespada.marvel.repositories

import com.efraespada.marvel.repositories.base.BaseHeroesRepositoryUnitTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class HeroesRepositoryUnitTest : BaseHeroesRepositoryUnitTest() {

    @Test
    fun `Get heroes`() = runBlocking {
        val heroes = repository.getHeroesList()
        assertEquals(true, heroes.isNotEmpty())
    }

    @Test
    fun `Get first hero`() = runBlocking {
        val heroes = repository.getHeroesList()
        assertEquals(true, heroes.isNotEmpty())
        val hero = repository.getHero(heroes.first().id)
        assertEquals(true, hero != null)
    }
}
