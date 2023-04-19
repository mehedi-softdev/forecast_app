package com.mehedisoftdev.forecastapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mehedisoftdev.forecastapp.databinding.ActivityMainBinding
import com.mehedisoftdev.forecastapp.repository.Response
import com.mehedisoftdev.forecastapp.utils.Network
import com.mehedisoftdev.forecastapp.viewmodel.MainViewModel
import com.mehedisoftdev.forecastapp.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val MY_PERMISSIONS_REQUEST_LOCATION = 68

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // enable location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // taking user permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }

        val repo = (application as WeatherApplication).repository

        binding.lastUpdateMessageTextView.text = application.getString(R.string.last_update_time_string)
                                                .plus(" ")
                                                .plus((application as WeatherApplication).getIntervalTime().toString())
        mainViewModel = ViewModelProvider(this,
                    MainViewModelFactory(this.applicationContext, repo))[MainViewModel::class.java]

        mainViewModel.weatherData.observe(this, Observer { response ->
            when(response) {
                is Response.Loading -> {
                    binding.nameTextView.text = getString(R.string.loading)
                    binding.tempTextView.text = getString(R.string.loading)
                    binding.feelsLikeTemp.text = getString(R.string.loading)
                    binding.humidityValue.text = getString(R.string.loading)
                    binding.windSpeedValue.text = getString(R.string.loading)
                }
                is Response.Success -> {
                    response.data?.let {
                        // update data
                        binding.nameTextView.text = it.name
                        binding.tempTextView.text = getTempFormat(it.main.temp.toString())
                        binding.feelsLikeTemp.text = getTempFormat(it.main.feels_like.toString())
                        binding.humidityValue.text = getHumidityFormat(it.main.humidity.toString())
                        binding.windSpeedValue.text = getWindSpeedFormat(it.wind.speed.toString())
                    }
                }
                is Response.Error -> {
                    response.errorMessage?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


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