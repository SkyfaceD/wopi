package org.skyfaced.wopi.repository.detail

import kotlinx.coroutines.flow.Flow
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.utils.Response

interface DetailRepository {
    fun getDetails(woeid: Int): Flow<Response<List<DetailItem>>>
}