package com.waracle.cakes.data.repository

import com.waracle.cakes.data.network.NetworkMonitor
import com.waracle.cakes.data.remote.CakeApi
import com.waracle.cakes.data.remote.dto.toDomain
import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.DataError
import com.waracle.cakes.domain.model.Outcome
import com.waracle.cakes.domain.repository.CakeRepository
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

// TODO: cache the last response so the list works offline
class CakeRepositoryImpl(
    private val api: CakeApi,
    private val networkMonitor: NetworkMonitor,
) : CakeRepository {

    override suspend fun getCakes(): Outcome<List<Cake>> {
        if (!networkMonitor.isOnline()) return Outcome.Failure(DataError.NoInternet)

        return try {
            Outcome.Success(api.fetchCakes().mapNotNull { it.toDomain() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: ResponseException) {
            Outcome.Failure(DataError.Server(e.response.status.value))
        } catch (e: IOException) { // dropped connection, timeout...
            Outcome.Failure(DataError.Network)
        } catch (e: SerializationException) {
            Outcome.Failure(DataError.InvalidData)
        } catch (e: Exception) {
            Outcome.Failure(DataError.Unknown) // TODO: report to crash reporting
        }
    }
}
