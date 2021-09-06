package org.skyfaced.wopi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.skyfaced.wopi.model.Temperature
import org.skyfaced.wopi.model.WeatherState
import org.skyfaced.wopi.model.adapter.DashboardItem

@Entity(tableName = "dashboard")
data class DashboardEntity(
    @PrimaryKey
    val woeid: Int,
    val weatherState: WeatherState,
    val city: String,
    val temperature: Temperature,
) {
    fun toDashboardItem() = DashboardItem(
        woeid = woeid,
        weatherState = weatherState,
        city = city,
        temperature = temperature
    )
}