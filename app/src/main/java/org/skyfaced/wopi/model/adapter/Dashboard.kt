package org.skyfaced.wopi.model.adapter

data class DashboardHeader(
    val title: String,
) : Item

data class DashboardItem(
    val id: Int,
    val city: String,
    val imageUrl: String,
    val temperature: String,
) : Item

class DashboardAdd : Item

class Temperature(
    val centigrade: Double,
) {
    val fahrenheit: Double = (centigrade * 9 / 5) + 32
    val kelvin: Double = centigrade + 273.15

    val strCentigrade = "${centigrade.round(1)}°C"
    val strFahrenheit = "${fahrenheit.round(1)}°F"
    val strKelvin = "${kelvin.round(1)}K"

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}