package com.efraespada.marvel.model.credentials

import com.efraespada.marvel.R
import com.stringcare.library.reveal

class CredentialsProviderImpl : CredentialsProvider {
    override fun getApiKey() = R.string.apiKey.reveal()

    override fun getPrivateKey() = R.string.privateKey.reveal()
}
