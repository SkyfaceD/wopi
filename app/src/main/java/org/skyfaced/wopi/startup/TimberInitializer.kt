package org.skyfaced.wopi.startup

import android.content.Context
import androidx.startup.Initializer
import org.skyfaced.wopi.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            Timber.d("Timber initialized")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}