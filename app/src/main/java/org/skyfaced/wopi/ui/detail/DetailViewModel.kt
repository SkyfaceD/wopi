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
import org.skyfaced.wopi.database.entity.DashboardEntity
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.repository.detail.DetailRepositoryImpl
import org.skyfaced.wopi.utils.State
import org.skyfaced.wopi.utils.di.ViewModelAssistedFactory
import org.skyfaced.wopi.utils.extensions.loading
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepositoryImpl,
    @Assisted
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val woeid = savedStateHandle.get<Int>(DetailFragment.BUNDLE_KEY_WOEID)
        ?: throw IllegalArgumentException("Woeid not passed")

    private val _detailsResponse = MutableStateFlow<State<List<DetailItem>>>(loading())
    val detailsResponse = _detailsResponse.asStateFlow()

    private val _details = mutableListOf<DetailItem>()
    val details get() = _details.toList()

    private var _favoriteState = MutableStateFlow(false)
    val favoriteState get() = _favoriteState.asStateFlow()

    init {
        Timber.e("woeid: $woeid")
        viewModelScope.launch {
            checkFavoriteState()
            getLocation()
        }
    }

    fun retry() {
        viewModelScope.launch {
            getLocation()
        }
    }

    fun updateDetails(newDetails: List<DetailItem>) {
        _details.clear()
        _details.addAll(newDetails)
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    fun onFavoriteClick(city: String) {
        Timber.e("onFavoriteClick")
        viewModelScope.launch {
            val isFavorite = favoriteState.value
            when (isFavorite) {
                true -> detailRepository.removeFromFavorite(woeid)
                false -> {
                    val detailItem = details[0]
                    val entity = DashboardEntity(
                        woeid = woeid,
                        weatherState = detailItem.weatherState,
                        city = city,
                        temperature = detailItem.helper.temperature
                    )
                    detailRepository.addToFavorite(entity)
                }
            }
            checkFavoriteState()
        }
    }

    private suspend fun getLocation() {
        _detailsResponse.emitAll(detailRepository.getDetails(woeid))
    }

    private suspend fun checkFavoriteState() {
        _favoriteState.emit(detailRepository.isFavorite(woeid))
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<DetailViewModel>
}