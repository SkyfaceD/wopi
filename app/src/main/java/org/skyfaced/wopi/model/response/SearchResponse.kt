package org.skyfaced.wopi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.skyfaced.wopi.model.adapter.SearchItem
import org.skyfaced.wopi.utils.extensions.round

@Serializable
data class SearchResponse(
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
        description = listOfNotNull(
            locationType,
            lattLong.split(',')
                .map { it.toDouble().round() }
                .joinToString(", "),
            distance?.toString()
        ).joinToString(" • ")
    )
}