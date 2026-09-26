package com.waracle.cakes.di

import com.waracle.cakes.data.network.AndroidNetworkMonitor
import com.waracle.cakes.data.network.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
}
