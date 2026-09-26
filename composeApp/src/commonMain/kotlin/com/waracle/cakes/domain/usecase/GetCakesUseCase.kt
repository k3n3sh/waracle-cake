package com.waracle.cakes.domain.usecase

import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.Outcome
import com.waracle.cakes.domain.model.map
import com.waracle.cakes.domain.repository.CakeRepository

class GetCakesUseCase(private val repository: CakeRepository) {

    suspend operator fun invoke(): Outcome<List<Cake>> {
        return repository.getCakes().map { cakes ->
            // Same title (ignoring case/spaces) = duplicate. First one wins.
            // TODO: locale-aware sorting if titles ever have accents
            cakes
                .distinctBy { it.title.trim().lowercase() }
                .sortedBy { it.title.trim().lowercase() }
        }
    }
}
