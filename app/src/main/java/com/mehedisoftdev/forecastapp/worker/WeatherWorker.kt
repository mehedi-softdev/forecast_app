package com.mehedisoftdev.forecastapp.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mehedisoftdev.forecastapp.WeatherApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherWorker(
    private val context: Context,
    private val parameters: WorkerParameters
) : Worker(context, parameters)
{
    override fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            // fetch data from api
            val lat = (context as WeatherApplication).lat
            val lon = context.lon
            val units = context.units
            val appid = context.appid

            context.repository.getWeatherData(lat, lon, units, appid)
        }

        return Result.success()
    }

}