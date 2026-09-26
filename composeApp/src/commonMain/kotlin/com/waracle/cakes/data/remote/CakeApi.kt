package com.waracle.cakes.data.remote

import com.waracle.cakes.data.remote.dto.CakeDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class CakeApi(private val client: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true }

    // The file is served as text/plain, so decode it ourselves.
    suspend fun fetchCakes(): List<CakeDto> {
        val body = client.get(CAKES_URL).bodyAsText()
        return json.decodeFromString(body)
    }

    private companion object {
        // TODO: move to build config per environment
        const val CAKES_URL =
            "https://raw.githubusercontent.com/Waracle/mobile-coding-test-api/refs/heads/main/cakes"
    }
}
