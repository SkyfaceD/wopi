package org.skyfaced.wopi.model.adapter

import org.skyfaced.wopi.model.LocationType

data class SearchItem(
    val id: Int,
    val city: String,
    val locationType: LocationType,
    val lattLong: String,
    val distance: Int? = null,
) : Item