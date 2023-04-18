package com.mehedisoftdev.forecastapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mehedisoftdev.forecastapp.repository.WeatherRepo

class MainViewModelFactory(private val context: Context,private val repo: WeatherRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(context, repo) as T
    }
}