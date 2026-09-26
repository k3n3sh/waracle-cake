package com.waracle.cakes.data.remote.dto

import com.waracle.cakes.domain.model.Cake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// All optional so one bad entry doesn't break the whole list.
@Serializable
data class CakeDto(
    val title: String? = null,
    @SerialName("desc") val description: String? = null,
    val image: String? = null,
)

// No title, nothing to show.
fun CakeDto.toDomain(): Cake? {
    val title = title?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return Cake(
        title = title,
        description = description?.trim().orEmpty(),
        imageUrl = image?.trim()?.takeIf { it.isNotEmpty() },
    )
}
