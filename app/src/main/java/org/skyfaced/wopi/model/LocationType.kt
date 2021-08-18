package org.skyfaced.wopi.model

import androidx.annotation.DrawableRes
import org.skyfaced.wopi.R

//TODO Set drawables
enum class LocationType(@DrawableRes val drawableRes: Int) {
    City(R.drawable.ic_search),
    RSP(R.drawable.ic_search),
    Country(R.drawable.ic_search),
    Continent(R.drawable.ic_search);

    companion object {
        fun extract(string: String) = when (string) {
            "City" -> City
            "Country" -> Country
            "Continent" -> Continent
            else -> RSP
        }
    }
}