package com.efraespada.marvel.repositories.builders

import com.efraespada.marvel.model.data.HeroApi
import com.efraespada.marvel.model.data.HeroRepository
import com.efraespada.marvel.repositories.credentials.CredentialsProviderImpl
import com.efraespada.marvel.repositories.extensions.service

fun heroRepository() = HeroRepository(
    HeroApi(
        HeroApi.Service::class.service(HeroApi.API_URL),
    ).also {
        it.credentialProvider = CredentialsProviderImpl()
    }
)
