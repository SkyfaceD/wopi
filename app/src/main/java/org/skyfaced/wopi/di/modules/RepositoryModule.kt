package org.skyfaced.wopi.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.repository.Weather
import org.skyfaced.wopi.repository.WeatherImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun weatherRepository(metaWeatherApi: MetaWeatherApi): Weather {
        return WeatherImpl(metaWeatherApi)
    }
}