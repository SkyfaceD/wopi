package org.skyfaced.wopi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.repository.DetailRepositoryImpl
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.di.ViewModelAssistedFactory
import org.skyfaced.wopi.utils.extensions.loading
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepositoryImpl,
    @Assisted
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _detailsResponse = MutableStateFlow<Response<List<DetailItem>>>(loading())
    val detailsResponse = _detailsResponse.asStateFlow()

    private val _details = mutableListOf<DetailItem>()
    val details get() = _details.toList()

    init {
        Timber.e("DetailViewModel Initialized")
        getLocation()
    }

    fun retry() = getLocation()

    fun updateDetails(newDetails: List<DetailItem>) {
        _details.clear()
        _details.addAll(newDetails)
    }

    private fun getLocation() {
        viewModelScope.launch {
            val woeid = savedStateHandle.get<Int>(DetailFragment.BUNDLE_KEY_WOEID)
                ?: throw IllegalArgumentException("Woeid not passed")
            Timber.d("Woeid: $woeid")
            _detailsResponse.emitAll(detailRepository.getDetails(woeid))
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<DetailViewModel>
}