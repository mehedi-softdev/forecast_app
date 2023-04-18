package com.mehedisoftdev.forecastapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mehedisoftdev.forecastapp.api.WeatherService
import com.mehedisoftdev.forecastapp.db.WeatherDatabase
import com.mehedisoftdev.forecastapp.models.*
import com.mehedisoftdev.forecastapp.utils.Network

class WeatherRepo(private val weatherService: WeatherService,
        private val weatherDatabase: WeatherDatabase) {
    private val weatherLiveData = MutableLiveData<Weather>()

    val weatherData: LiveData<Weather>
    get() = weatherLiveData

    suspend fun getWeatherData(lat: Double, lon: Double, units: String, appid: String) {
        if(Network.isInternetAvailable()) {
            val report = weatherService.getWeatherData(lat, lon, units, appid)
            if(report!=null) {
                // store as local cache
                // clear previous data
                try {
                    weatherDatabase.getWeatherDao().deleteWeatherData(
                        weatherDatabase.getWeatherDao().getWeatherData()
                    )
                    weatherDatabase.getWeatherDao().deleteMainData(
                        weatherDatabase.getWeatherDao().getMainData()
                    )
                    weatherDatabase.getWeatherDao().deleteWindData(
                        weatherDatabase.getWeatherDao().getWindData()
                    )
                }catch (e: Exception) {
                    // do nothing
                }
                // after clearing insert main and wind data
                weatherDatabase.getWeatherDao().insertWeatherData(
                    WeatherInfo(0, report.body()!!.name)
                )
                weatherDatabase.getWeatherDao()
                    .insertMain(report.body()!!.main)
                weatherDatabase.getWeatherDao().insertWind( report.body()!!.wind)


                // put on live data
                weatherLiveData.postValue(report.body())
            }
        } else {
            // get data from local cache
          try {
              val main: Main = weatherDatabase.getWeatherDao().getMainData()
              val wind: Wind = weatherDatabase.getWeatherDao().getWindData()
              val name: String = weatherDatabase.getWeatherDao().getWeatherData().name

              val weatherXList: List<WeatherX> = listOf(WeatherX("description", "icon", 1, "main"))

              // setting random value except main and wind
              val weather = Weather(
                  "base",
                  Clouds(1),
                  1,
                  Coord(1.0, 1.0),
                  1,
                  1,
                  main,
                  name,
                  Sys("country", 1, 1, 1, 1),
                  1,
                  1,
                  weatherXList,
                  wind
              )
              weatherLiveData.postValue(weather)
          }catch (e: Exception) {
              e.printStackTrace()
          }
        }

    }

    // background work function
    suspend fun backgroundFetchData(lat: Double,
                                    lon: Double,
                                    units: String,
                                    appid: String)
    {
        val report = weatherService.getWeatherData(lat, lon, units, appid)
        if(report?.body() != null) {
            // store data to local cache
            // clear previous data
            try {
                weatherDatabase.getWeatherDao().deleteWeatherData(
                    weatherDatabase.getWeatherDao().getWeatherData()
                )
                weatherDatabase.getWeatherDao().deleteMainData(
                    weatherDatabase.getWeatherDao().getMainData()
                )
                weatherDatabase.getWeatherDao().deleteWindData(
                    weatherDatabase.getWeatherDao().getWindData()
                )
            }catch (e: Exception) {
                // do nothing
            }
            // after clearing insert main and wind data
            weatherDatabase.getWeatherDao().insertWeatherData(
                WeatherInfo(0, report.body()!!.name)
            )
            weatherDatabase.getWeatherDao()
                .insertMain(report.body()!!.main)
            weatherDatabase.getWeatherDao().insertWind( report.body()!!.wind)

        }
    }
}