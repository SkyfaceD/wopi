package org.skyfaced.wopi.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.joda.time.DateTime
import org.skyfaced.wopi.model.WeatherState
import org.skyfaced.wopi.model.adapter.DetailHelper
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.onResponse
import org.skyfaced.wopi.utils.extensions.success
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
) : DetailRepository {
    override fun getDetails(woeid: Int): Flow<Response<List<DetailItem>>> {
        return flow<Response<List<DetailItem>>> {
            val result = metaWeatherApi.getLocation(woeid)

            if (result.isFailure()) {
                throw result.error
            }

            val details = mutableListOf<DetailItem>()
            for (response in result.asSuccess().value.consolidatedWeather) {
                details.add(
                    DetailItem(
                        weatherState = WeatherState.fromAbbreviation(response.weatherStateAbbr),
                        date = DateTime.parse(response.applicableDate),
                        helper = DetailHelper(
                            temperature = response.theTemp,
                            windSpeed = response.windSpeed,
                            airPressure = response.airPressure,
                            humidity = response.humidity,
                            visibility = response.visibility ?: 0.0
                        )
                    )
                )
            }

            emit(success(details))
        }.onResponse().flowOn(Dispatchers.IO)
    }
}