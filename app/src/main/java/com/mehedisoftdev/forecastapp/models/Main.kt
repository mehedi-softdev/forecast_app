package com.mehedisoftdev.forecastapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main")
data class Main(
    @PrimaryKey(autoGenerate = true)
    val mainId: Long,
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)