package com.mehedisoftdev.forecastapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mehedisoftdev.forecastapp.databinding.ActivityMainBinding
import com.mehedisoftdev.forecastapp.utils.Network
import com.mehedisoftdev.forecastapp.viewmodel.MainViewModel
import com.mehedisoftdev.forecastapp.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = (application as WeatherApplication).repository
        binding.lastUpdateMessageTextView.text = application.getString(R.string.last_update_time_string)
                                                .plus(" ")
                                                .plus((application as WeatherApplication).getIntervalTime().toString())
        mainViewModel = ViewModelProvider(this,
                    MainViewModelFactory(this.applicationContext, repo))[MainViewModel::class.java]

        mainViewModel.weatherData.observe(this, Observer {
            // update data
            binding.nameTextView.text = it.name
            binding.tempTextView.text = getTempFormat(it.main.temp.toString())
            binding.feelsLikeTemp.text = getTempFormat(it.main.feels_like.toString())
            binding.humidityValue.text = getHumidityFormat(it.main.humidity.toString())
            binding.windSpeedValue.text = getWindSpeedFormat(it.wind.speed.toString())
        })

        binding.btnRefresh.setOnClickListener {
            // update data
            mainViewModel.reload()
            Toast.makeText(this, "Data refreshed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getTempFormat(temp: String) : String? {
        return "$temp °C"
    }

    private fun getWindSpeedFormat(data: String): String? {
        return "$data m/s"
    }

    private fun getHumidityFormat(data: String): String? {
        return "$data %"
    }

}