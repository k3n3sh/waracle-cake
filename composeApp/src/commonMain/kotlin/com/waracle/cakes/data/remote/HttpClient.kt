package com.waracle.cakes.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout

// Engine comes from the platform module (OkHttp / Darwin), or MockEngine in tests.
fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        expectSuccess = true // non-2xx throws
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 20_000
        }
        // TODO: retry with backoff, logging in debug builds
    }
}
