package org.skyfaced.wopi.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.repository.DetailRepositoryImpl
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.error
import org.skyfaced.wopi.utils.extensions.loading
import org.skyfaced.wopi.utils.extensions.success

class DetailViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepositoryImpl,
    @Assisted private val woeid: Int,
) : ViewModel() {
    private val _detail = MutableSharedFlow<Response<DetailRepositoryImpl.Detail>>()
    val detail = _detail.asSharedFlow()

    init {
        getLocation()
    }

    fun refresh() {
        getLocation()
    }

    private fun getLocation() {
        viewModelScope.launch {
            with(_detail) {
                emit(loading())
                detailRepository.fetch(woeid).catch { t ->
                    emit(error(cause = t))
                }.collect { detail ->
                    emit(success(detail))
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted woeid: Int): DetailViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: Factory, woeid: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return assistedFactory.create(woeid) as T
                }
            }
    }
}