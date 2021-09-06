package org.skyfaced.wopi.model

import kotlin.math.roundToInt

/**
 * Property result examples:
 * @property celsius - '23℃'
 * @property fahrenheit - '73.4℉'
 * @property kelvin - '296.15K'
 */
class Temperature(val centigrade: Double) {
    val celsius = "${centigrade.roundToInt()}℃"
    val fahrenheit = "${((centigrade * 9 / 5) + 32).roundToInt()}℉"
    val kelvin = "${(centigrade + 273.15).roundToInt()}K"

    override fun toString(): String {
        return "Temperature(celsius=$celsius, fahrenheit=$fahrenheit, kelvin=$kelvin)"
    }
}