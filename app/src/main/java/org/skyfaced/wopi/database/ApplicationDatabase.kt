package org.skyfaced.wopi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.skyfaced.wopi.database.converter.TemperatureConverter
import org.skyfaced.wopi.database.converter.WeatherStateConverter
import org.skyfaced.wopi.database.dao.DashboardDao
import org.skyfaced.wopi.database.entity.DashboardEntity


@Database(entities = [DashboardEntity::class], version = 1, exportSchema = true)
@TypeConverters(WeatherStateConverter::class, TemperatureConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
}