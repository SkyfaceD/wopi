package org.skyfaced.wopi.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.skyfaced.wopi.model.adapter.SearchItem
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.repository.SearchRepository
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.error
import org.skyfaced.wopi.utils.extensions.isCoordinates
import org.skyfaced.wopi.utils.extensions.loading
import org.skyfaced.wopi.utils.extensions.success
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {
    private val _searchResult = MutableStateFlow<Response<List<SearchItem>>>(success(emptyList()))
    val searchResult = _searchResult.asStateFlow()

    private var _currentQuery = ""
    val currentQuery get() = _currentQuery

    fun saveQueryAndStartSearching(query: String) {
        _currentQuery = query
        search(query)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            with(_searchResult) {
                emit(loading())

                val result =
                    if (query.isCoordinates) searchRepository.searchByCoordinates(query)
                    else searchRepository.searchByLocation(query)

                if (result.isFailure()) {
                    emit(error(result))
                } else {
                    emit(success(result.asSuccess().value.map(Search::toSearchItem)))
                }
            }
        }
    }
}