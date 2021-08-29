package org.skyfaced.wopi.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.repository.DetailRepositoryImpl
import org.skyfaced.wopi.repository.SearchRepository
import org.skyfaced.wopi.repository.SearchRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun searchRepository(metaWeatherApi: MetaWeatherApi): SearchRepository {
        return SearchRepositoryImpl(metaWeatherApi)
    }

    @Provides
    @ViewModelScoped
    fun detailRepository(metaWeatherApi: MetaWeatherApi): DetailRepositoryImpl {
        return DetailRepositoryImpl(metaWeatherApi)
    }
}