package com.efraespada.data.model

data class HeroModel(
    val id: String,
    val name: String,
    val description: String,
    val resourceURI: String,
    val thumbnail: Image,
    val comics: Comics,
)

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
