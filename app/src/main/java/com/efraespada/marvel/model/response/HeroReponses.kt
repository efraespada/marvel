package com.efraespada.marvel.model.response

data class HeroesPagination(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Hero>
)

data class HeroesResponse(val data: HeroesPagination)

data class Image(
    val path: String,
    val extension: String,
)

data class Comic(
    val resourceURI: String,
    val name: String,
)

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Comic>,
)

data class Hero(
    val id: String,
    val name: String,
    val description: String,
    val resourceURI: String,
    val thumbnail: Image,
    val comics: Comics,
)
