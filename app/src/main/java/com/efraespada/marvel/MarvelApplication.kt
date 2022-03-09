package com.efraespada.marvel

import android.app.Application
import com.stringcare.library.SC
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SC.init { applicationContext }
    }
}
