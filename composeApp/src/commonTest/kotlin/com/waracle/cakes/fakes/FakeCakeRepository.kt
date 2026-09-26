package com.waracle.cakes.fakes

import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.Outcome
import com.waracle.cakes.domain.repository.CakeRepository

class FakeCakeRepository(
    var result: Outcome<List<Cake>> = Outcome.Success(emptyList()),
) : CakeRepository {
    override suspend fun getCakes(): Outcome<List<Cake>> {
        return result
    }
}
