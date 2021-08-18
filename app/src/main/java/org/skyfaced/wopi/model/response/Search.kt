package org.skyfaced.wopi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.skyfaced.wopi.model.LocationType
import org.skyfaced.wopi.model.adapter.SearchItem

@Serializable
data class Search(
    val woeid: Int,
    val title: String,
    @SerialName("location_type")
    val locationType: String,
    @SerialName("latt_long")
    val lattLong: String,
    val distance: Int? = null,
) {
    fun toSearchItem() = SearchItem(
        id = woeid,
        city = title,
        locationType = LocationType.extract(locationType),
        lattLong = lattLong,
        distance = distance
    )
}