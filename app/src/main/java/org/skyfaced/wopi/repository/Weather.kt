package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.Search

interface Weather {
    suspend fun searchByLocation(location: String): List<Search>
}