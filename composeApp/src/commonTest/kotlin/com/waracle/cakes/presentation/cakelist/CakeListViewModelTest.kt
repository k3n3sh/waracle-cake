package com.waracle.cakes.presentation.cakelist

import androidx.lifecycle.SavedStateHandle
import com.waracle.cakes.domain.model.DataError
import com.waracle.cakes.domain.model.Outcome
import com.waracle.cakes.domain.usecase.GetCakesUseCase
import com.waracle.cakes.fakes.FakeCakeRepository
import com.waracle.cakes.fakes.cake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CakeListViewModelTest {

    private val repository = FakeCakeRepository()

    private fun createViewModel(): CakeListViewModel {
        return CakeListViewModel(GetCakesUseCase(repository), SavedStateHandle())
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Required: app starts and loading works -> cakes shown, no duplicates, sorted by name
    @Test
    fun loadSucceeds_showsUniqueSortedCakes() =
        runTest {
            repository.result = Outcome.Success(
                listOf(cake("Victoria Sponge"), cake("Carrot Cake"), cake("Banana Cake"), cake("Carrot Cake")),
            )

            val viewModel = createViewModel()
            advanceUntilIdle()

            assertEquals(
                CakeListUiState(
                    cakes = listOf(cake("Banana Cake"), cake("Carrot Cake"), cake("Victoria Sponge")),
                    loadCount = 1,
                ),
                viewModel.state.value,
                "list should be de-duplicated and sorted",
            )
        }

    // Required: app starts and loading fails -> error shown
    @Test
    fun loadFails_showsError() = runTest {
        repository.result = Outcome.Failure(DataError.Network)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(CakeListUiState(error = DataError.Network), viewModel.state.value, "error should be shown")
    }

    // Retry after a failed load shows the cakes
    @Test
    fun retryAfterFailure_showsCakes() = runTest {
        repository.result = Outcome.Failure(DataError.Network)
        val viewModel = createViewModel()
        advanceUntilIdle()

        repository.result = Outcome.Success(listOf(cake("Rocky Road")))
        viewModel.loadCakes()
        advanceUntilIdle()

        assertEquals(
            CakeListUiState(cakes = listOf(cake("Rocky Road")), loadCount = 1),
            viewModel.state.value,
            "retry should load the cakes",
        )
    }
}
