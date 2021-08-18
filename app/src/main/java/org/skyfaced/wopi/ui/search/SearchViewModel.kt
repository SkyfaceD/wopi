package org.skyfaced.wopi.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.repository.Weather
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.handle
import org.skyfaced.wopi.utils.extensions.load
import org.skyfaced.wopi.utils.extensions.success
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val weather: Weather) : ViewModel() {
    private val _searchLocation =
        MutableStateFlow<Response<List<Search>>>(success(emptyList()))
    val searchLocation = _searchLocation.asStateFlow()

    private var searchQuery = ""

    fun searchByLocation(location: String = searchQuery) {
        viewModelScope.launch {
            _searchLocation.emit(load())
            _searchLocation.emit(handle(weather.searchByLocation(location)))
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
}