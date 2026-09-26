package com.waracle.cakes.di

import com.waracle.cakes.data.network.IosNetworkMonitor
import com.waracle.cakes.data.network.NetworkMonitor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single<NetworkMonitor> { IosNetworkMonitor() }
}
