package com.mehedisoftdev.forecastapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mehedisoftdev.forecastapp.models.Main
import com.mehedisoftdev.forecastapp.models.WeatherInfo
import com.mehedisoftdev.forecastapp.models.Wind

const val DB_NAME = "weather.db"

@Database(entities = [WeatherInfo::class,Main::class, Wind::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getWeatherDao() : WeatherDao

    companion object {
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            if(instance == null) {
                // initialize
                synchronized(this) {
                    instance = Room.databaseBuilder(
                     context, WeatherDatabase::class.java,
                     DB_NAME
                    ).build()
                }
            }
            return instance!!
        }
    }
}