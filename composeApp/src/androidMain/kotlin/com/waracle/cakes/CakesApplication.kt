package com.waracle.cakes

import android.app.Application
import com.waracle.cakes.di.initKoin
import org.koin.android.ext.koin.androidContext

class CakesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CakesApplication)
        }
    }
}
