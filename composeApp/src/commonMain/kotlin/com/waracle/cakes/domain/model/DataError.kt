package com.waracle.cakes.domain.model

sealed interface DataError {
    data object NoInternet : DataError
    data object Network : DataError
    data class Server(val statusCode: Int) : DataError
    data object InvalidData : DataError
    data object Unknown : DataError
}
