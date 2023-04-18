package com.mehedisoftdev.forecastapp

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.mehedisoftdev.forecastapp.api.RetrofitHelper
import com.mehedisoftdev.forecastapp.api.WeatherService
import com.mehedisoftdev.forecastapp.db.WeatherDatabase
import com.mehedisoftdev.forecastapp.repository.WeatherRepo
import com.mehedisoftdev.forecastapp.worker.WeatherWorker
import java.util.concurrent.TimeUnit


class WeatherApplication : Application() {

    lateinit var repository: WeatherRepo
    // for weather report
    lateinit var units : String
    var lat: Double = 0.0
    var lon: Double = 0.0
    lateinit var appid: String
    private var interValTime: Long = 25

    fun getIntervalTime(): Long = interValTime

    override fun onCreate() {
        super.onCreate()
        initialize()
        doBackgroundWork()
    }

    private fun doBackgroundWork() {
        // constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workerReq = PeriodicWorkRequest.Builder(
            WeatherWorker::class.java,
            interValTime,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

    }

    private fun initialize() {
        units = "metric" // information measure unit
        // lat and lon value according to my location
        lat = 29.047200
        lon = 77.878800
        appid = "d9c4b1e0bb7590108d74467e820c223b"


        val weatherService = RetrofitHelper
                            .getInstance()
                            .create(WeatherService::class.java)
        val weatherDatabase = WeatherDatabase.getInstance(applicationContext)
        repository = WeatherRepo(weatherService, weatherDatabase)
    }


}