package com.mehedisoftdev.forecastapp.db

import androidx.room.*
import com.mehedisoftdev.forecastapp.models.Main
import com.mehedisoftdev.forecastapp.models.WeatherInfo
import com.mehedisoftdev.forecastapp.models.Wind

@Dao
interface WeatherDao {
    // main dao
    @Insert
    suspend fun insertMain(main: Main)

    @Query("SELECT * FROM main")
    suspend fun getMainData() : Main

    @Delete
    suspend fun deleteMainData(main: Main)

    @Update
    suspend fun updateMain(main: Main)
    // wind dao
    @Insert
    suspend fun insertWind(wind: Wind)

    @Query("SELECT * FROM wind")
    suspend fun getWindData() : Wind

    @Delete
    suspend fun deleteWindData(wind: Wind)

    @Update
    suspend fun updateWind(wind: Wind)

    // weather dao
    @Insert
    suspend fun insertWeatherData(weather: WeatherInfo)

    @Query("SELECT * FROM weather")
    suspend fun getWeatherData(): WeatherInfo

    @Delete
    suspend fun deleteWeatherData(weather: WeatherInfo)

    @Update
    suspend fun updateWeatherData(weather: WeatherInfo)

}