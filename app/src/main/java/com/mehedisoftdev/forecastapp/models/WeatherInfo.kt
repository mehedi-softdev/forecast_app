package com.mehedisoftdev.forecastapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


// copy version of weather class
@Entity(tableName = "weather")
data class WeatherInfo(
    @PrimaryKey(autoGenerate = true)
    val weatherId: Long,
    val name: String
)