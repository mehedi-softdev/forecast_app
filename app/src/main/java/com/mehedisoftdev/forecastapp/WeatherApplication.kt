package com.mehedisoftdev.forecastapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mehedisoftdev.forecastapp.api.RetrofitHelper
import com.mehedisoftdev.forecastapp.api.WeatherService
import com.mehedisoftdev.forecastapp.db.WeatherDatabase
import com.mehedisoftdev.forecastapp.repository.WeatherRepo
import com.mehedisoftdev.forecastapp.worker.WeatherWorker
import java.util.concurrent.TimeUnit


class WeatherApplication: Application() {
    var activity: Activity? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val appId: String = "d9c4b1e0bb7590108d74467e820c223b"
    lateinit var repository: WeatherRepo
    // for weather report
    lateinit var units : String
    var lat: Double? = 0.0
    var lon: Double? = 0.0
    private var interValTime: Long = 25

    fun getIntervalTime(): Long = interValTime
    fun getAppId() : String = appId

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

    @SuppressLint("MissingPermission")
    private fun initialize() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        refreshLocation()
        units = "metric" // information measure unit

        val weatherService = RetrofitHelper
                            .getInstance()
                            .create(WeatherService::class.java)
        val weatherDatabase = WeatherDatabase.getInstance(applicationContext)
        repository = WeatherRepo(weatherService, weatherDatabase)
    }

    @SuppressLint("MissingPermission")
    fun refreshLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                lat = location?.latitude
                lon = location?.longitude
            }
    }


}