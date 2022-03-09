package com.efraespada.data.model


data class HeroesPagination(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<HeroModel>
)

data class HeroesResponse(val data: HeroesPagination)

