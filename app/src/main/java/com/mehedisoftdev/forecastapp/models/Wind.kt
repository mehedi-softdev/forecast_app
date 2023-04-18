package com.mehedisoftdev.forecastapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wind")
data class Wind(
    @PrimaryKey(autoGenerate = true)
    val windId: Long,
    val deg: Int,
    val gust: Double,
    val speed: Double
)