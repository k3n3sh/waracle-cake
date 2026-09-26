package com.waracle.cakes.data.repository

import com.waracle.cakes.data.network.NetworkMonitor
import com.waracle.cakes.data.remote.CakeApi
import com.waracle.cakes.data.remote.createHttpClient
import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.DataError
import com.waracle.cakes.domain.model.Outcome
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class CakeRepositoryImplTest {

    private fun repository(online: Boolean = true, handler: MockRequestHandler): CakeRepositoryImpl {
        val networkMonitor = object : NetworkMonitor {
            override suspend fun isOnline(): Boolean {
                return online
            }
        }
        return CakeRepositoryImpl(
            api = CakeApi(createHttpClient(MockEngine(handler))),
            networkMonitor = networkMonitor,
        )
    }

    // JSON is mapped to cakes; entries without a title are skipped
    @Test
    fun success_mapsCakesAndSkipsUntitled() = runTest {
        val repository = repository {
            respond("""[{"title": "Dundee Cake", "desc": "A staple", "image": "https://x.com/d.jpg"}, {"desc": "?"}]""")
        }

        assertEquals(
            Outcome.Success(listOf(Cake("Dundee Cake", "A staple", "https://x.com/d.jpg"))),
            repository.getCakes(),
            "response should map to cakes",
        )
    }

    // Offline -> no request is made at all
    @Test
    fun offline_returnsNoInternetWithoutRequest() = runTest {
        val repository = repository(online = false) { fail("request should not be made") }

        assertEquals(Outcome.Failure(DataError.NoInternet), repository.getCakes(), "should report no internet")
    }
}
