package com.will.busnotification.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.will.busnotification.data.model.TransitSegment
import com.will.busnotification.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchLineViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SearchLineViewModel"
        private const val MIN_QUERY_LENGTH = 3
        private const val DEBOUNCE_MS = 600L
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchResults = MutableStateFlow<List<TransitSegment>>(emptyList())
    val searchResults: StateFlow<List<TransitSegment>> = _searchResults.asStateFlow()

    /** Null = no error; non-null = user-facing error message */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /** True when search completed successfully but returned zero results */
    private val _emptyResults = MutableStateFlow(false)
    val emptyResults: StateFlow<Boolean> = _emptyResults.asStateFlow()

    init {
        _searchQuery
            .debounce(DEBOUNCE_MS)
            .onEach { query ->
                _errorMessage.value = null
                _emptyResults.value = false

                if (query.isBlank() || query.length < MIN_QUERY_LENGTH) {
                    _searchResults.value = emptyList()
                    _isSearching.value = false
                    return@onEach
                }

                _isSearching.value = true
                viewModelScope.launch {
                    try {
                        val results = placesRepository.searchPlaces(query)
                        _searchResults.value = results
                        _emptyResults.value = results.isEmpty()

                        if (results.isEmpty()) {
                            Log.d(TAG, "No results for query: '$query'")
                        } else {
                            Log.d(TAG, "Found ${results.size} results for '$query'")
                        }
                    } catch (e: IllegalStateException) {
                        // Location unavailable
                        Log.e(TAG, "Location error", e)
                        _searchResults.value = emptyList()
                        _errorMessage.value = "Localização indisponível. Verifique se o GPS está ativado e a permissão concedida."
                    } catch (e: Exception) {
                        Log.e(TAG, "searchPlaces failed", e)
                        _searchResults.value = emptyList()
                        _errorMessage.value = "Erro ao buscar rotas. Verifique sua conexão e tente novamente."
                    } finally {
                        _isSearching.value = false
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
