package org.skyfaced.wopi.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.skyfaced.wopi.database.ApplicationDatabase
import org.skyfaced.wopi.database.dao.DashboardDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private const val DATABASE_NAME = "wopi-db"

    @Provides
    @Singleton
    fun database(application: Application): ApplicationDatabase {
        return Room.databaseBuilder(application, ApplicationDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun dashboardDao(database: ApplicationDatabase): DashboardDao {
        return database.dashboardDao()
    }
}