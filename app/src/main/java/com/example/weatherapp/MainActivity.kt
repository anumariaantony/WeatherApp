package com.example.weatherapp

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel


class   MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var geocoder : Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        geocoder = Geocoder(this)
        weatherViewModel = WeatherViewModel(geocoder)

        binding.weatherViewModel = weatherViewModel

        binding.lifecycleOwner = this

    }
}