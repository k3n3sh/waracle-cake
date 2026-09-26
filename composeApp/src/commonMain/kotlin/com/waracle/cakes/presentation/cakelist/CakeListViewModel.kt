package com.waracle.cakes.presentation.cakelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.domain.model.DataError
import com.waracle.cakes.domain.model.Outcome
import com.waracle.cakes.domain.usecase.GetCakesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CakeListUiState(
    val cakes: List<Cake> = emptyList(),
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val selectedCake: Cake? = null,
    // bumped on every successful load, so the list animates in again
    val loadCount: Int = 0,
)

// Loads once on creation. The ViewModel outlives rotation, so the list isn't reloaded.
class CakeListViewModel(
    private val getCakes: GetCakesUseCase,
    private val savedState: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(CakeListUiState())
    val state: StateFlow<CakeListUiState> = _state.asStateFlow()

    init {
        loadCakes()
    }

    // Also used for refresh and retry.
    fun loadCakes() {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = getCakes()) {
                is Outcome.Success -> _state.update {
                    // reopens the popup after process death
                    val selectedTitle: String? = savedState[SELECTED_CAKE]
                    val selected = result.data.find { cake -> cake.title == selectedTitle }
                    it.copy(isLoading = false, cakes = result.data, selectedCake = selected, loadCount = it.loadCount + 1)
                }
                // keep whatever list we already have
                is Outcome.Failure -> _state.update { it.copy(isLoading = false, error = result.error) }
            }
        }
    }

    // null closes the popup
    fun selectCake(cake: Cake?) {
        savedState[SELECTED_CAKE] = cake?.title
        _state.update { it.copy(selectedCake = cake) }
    }

    // TODO: retry automatically when the device comes back online

    private companion object {
        const val SELECTED_CAKE = "selected_cake"
    }
}
