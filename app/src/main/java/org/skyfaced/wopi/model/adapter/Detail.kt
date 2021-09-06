package org.skyfaced.wopi.model.adapter

import org.joda.time.DateTime
import org.skyfaced.wopi.model.Temperature
import org.skyfaced.wopi.model.WeatherState
import org.skyfaced.wopi.utils.extensions.lazySafetyNone
import org.skyfaced.wopi.utils.extensions.round
import java.util.*
import kotlin.math.roundToInt

data class DetailItem(
    val weatherState: WeatherState,
    val date: DateTime,
    val helper: DetailHelper,
    val week: Week = Week(date.toString("EEE", Locale.ENGLISH)),
) {
    val dateStr: String by lazySafetyNone { date.toString("EEEE, dd MMMM", Locale.ENGLISH) }
}

data class Week(
    val dayOfWeek: String,
    val isChecked: Boolean = false,
)

/**
 * @param windSpeed default in mph
 * @param visibility default in miles
 *
 * Property result examples:
 * @property windMph - '3 mph'
 * @property windKph - '6 kph'
 * @property airPressureStr - '1022 mbar'
 * @property humidityStr - '40%'
 * @property visibilityMiles - '18.4 miles'
 * @property visibilityKilometers - '29.6 kilometers'
 */
class DetailHelper(
    val temperature: Temperature,
    windSpeed: Double,
    airPressure: Double,
    humidity: Int,
    visibility: Double,
) {
    val windMph by lazySafetyNone { "${windSpeed.roundToInt()} mph" }
    val windKph by lazySafetyNone { "${windSpeed / 1.60934} kph" }

    val airPressureStr = "${airPressure.roundToInt()} mbar"

    val humidityStr = "$humidity%"

    val visibilityMiles by lazySafetyNone { "${visibility.round(1)} miles" }
    val visibilityKilometers by lazySafetyNone { "${(visibility / 1.60934).round(1)} kilometers" }
}