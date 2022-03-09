package com.efraespada.domain.extenstions

import com.google.gson.Gson

inline fun <T, reified U> T.to(): U {
    val gson = Gson()
    val value = gson.toJson(this)
    return gson.fromJson(value, U::class.java)
}

inline fun <T, reified U> List<T>.toList() = this.map { it.to<T, U>() }