package com.efraespada.marvel.model.credentials

interface CredentialsProvider {
    fun getApiKey(): String
    fun getPrivateKey(): String
}
