package com.waracle.cakes.domain.model

// Like kotlin.Result, but with a typed error.
sealed interface Outcome<out T> {
    data class Success<out T>(val data: T) : Outcome<T>
    data class Failure(val error: DataError) : Outcome<Nothing>
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> {
    return when (this) {
        is Outcome.Success -> Outcome.Success(transform(data))
        is Outcome.Failure -> this
    }
}
