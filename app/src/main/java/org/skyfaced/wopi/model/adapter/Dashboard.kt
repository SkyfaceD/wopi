package org.skyfaced.wopi.model.adapter

import org.skyfaced.wopi.database.entity.DashboardEntity
import org.skyfaced.wopi.model.Temperature
import org.skyfaced.wopi.model.WeatherState

data class DashboardHeader(
    val title: String,
) : Item

data class DashboardItem(
    val woeid: Int,
    val weatherState: WeatherState,
    val city: String,
    val temperature: Temperature,
) : Item {
    fun toDashboardEntity() = DashboardEntity(
        woeid = woeid,
        weatherState = weatherState,
        city = city,
        temperature = temperature
    )
}