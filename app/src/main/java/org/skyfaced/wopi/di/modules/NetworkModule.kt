package org.skyfaced.wopi.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.result.ResultAdapterFactory
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val DEFAULT_TIMEOUT = 15_000L
    private const val META_WEATHER_URL = "https://www.metaweather.com/"

    private val json = Json { ignoreUnknownKeys = true }
    private val jsonConverter = json.asConverterFactory("application/json".toMediaType())

    private val logInterceptor = HttpLoggingInterceptor { message ->
        Timber.tag("OkHttp").d(message)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val metaWeatherClient = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()

    @Provides
    @Singleton
    fun metaWeatherApi(): MetaWeatherApi {
        return Retrofit.Builder()
            .baseUrl(META_WEATHER_URL)
            .client(metaWeatherClient)
            .addConverterFactory(jsonConverter)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
            .create(MetaWeatherApi::class.java)
    }
}