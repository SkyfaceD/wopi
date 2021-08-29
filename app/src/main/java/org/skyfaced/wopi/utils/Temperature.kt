package org.skyfaced.wopi.utils

import kotlin.math.roundToInt

/**
 * Draft
 */
class Temperature(private val celsiusDouble: Double) {
    val celsius = "${celsiusDouble.roundToInt()}℃"
    val kelvin = "${(celsiusDouble + 273.15).roundToInt()}K"
    val fahrenheit = "${((celsiusDouble * 9 / 5) + 32).roundToInt()}℉"
}