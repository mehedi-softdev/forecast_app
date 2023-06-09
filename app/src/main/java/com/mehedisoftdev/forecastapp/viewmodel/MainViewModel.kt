package com.mehedisoftdev.forecastapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehedisoftdev.forecastapp.WeatherApplication
import com.mehedisoftdev.forecastapp.models.Main
import com.mehedisoftdev.forecastapp.models.Weather
import com.mehedisoftdev.forecastapp.models.WeatherX
import com.mehedisoftdev.forecastapp.repository.Response
import com.mehedisoftdev.forecastapp.repository.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Context,
    private val repo: WeatherRepo
    ): ViewModel() {
    
    init {
        reload()
    }

    val weatherData: LiveData<Response<Weather>>
    get() = repo.weatherData

    fun reload() {
        Response.Loading<Weather>()
        (context as WeatherApplication).refreshLocation() // re locate location data
        viewModelScope.launch(Dispatchers.IO) {
            // fetch data from api
            val lat: Double? = (context as WeatherApplication).lat
            val lon: Double? = context.lon
            val units = context.units
            val appid = context.getAppId()

            repo.getWeatherData(lat!!, lon!!, units, appid)
        }
    }

}