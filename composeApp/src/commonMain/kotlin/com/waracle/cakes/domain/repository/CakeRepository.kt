package com.waracle.cakes.domain.repository

import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.Outcome

interface CakeRepository {
    suspend fun getCakes(): Outcome<List<Cake>>
}
