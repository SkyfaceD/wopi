package org.skyfaced.wopi.utils

enum class WeatherState(val abbreviation: String) {
    Snow("sn"),
    Sleet("sl"),
    Hail("h"),
    Thunderstorm("t"),
    HeavyRain("hr"),
    LightRain("lr"),
    Showers("s"),
    HeavyCloud("hc"),
    LightCloud("lc"),
    Clear("c")
}