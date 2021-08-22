package org.skyfaced.wopi.ui.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.skyfaced.wopi.repository.SearchRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

}