package com.waracle.cakes.fakes

import com.waracle.cakes.domain.model.Cake

fun cake(title: String): Cake {
    return Cake(
        title = title,
        description = "$title description",
        imageUrl = null,
    )
}
