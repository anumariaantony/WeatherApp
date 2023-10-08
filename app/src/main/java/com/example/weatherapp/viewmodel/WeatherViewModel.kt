package com.example.weatherapp.viewmodel

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.weatherapp.model.CurrentWeatherDetails
import com.example.weatherapp.model.Weather
import com.example.weatherapp.repository.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val  TAG = "WeatherViewModel"

class WeatherViewModel(geocoder : Geocoder) : ViewModel( ) {

    private val apikey = "cd8eb75dc9af795cbeb5e149d4bc0438"
    private val imageBaseUrl = "https://openweathermap.org/img/wn/"
    private val imageExtension = ".png"

    private val geocoder = geocoder

    //LiveData for showing the location in UI
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    //LiveData for showing the weather description in UI
    private val _weatherDescription = MutableLiveData<String>()
    val weatherDescription: LiveData<String> get() = _weatherDescription

    //LiveData for showing the weather image in UI
    private val _weatherIconUrl = MutableLiveData<String>()
    val weatherIconUrl: LiveData<String> get() = _weatherIconUrl

    //LiveData for showing the temperature in UI
    private val _temp = MutableLiveData<String>()
    val temp: LiveData<String> get() = _temp

    /**
     * Gets current weather details with API call
     */
    private fun getWeatherDetails(latitude : Double, longitude :Double, location: String) {

        var currentWeather : CurrentWeatherDetails
        var weatherCondition : Weather

        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.api.getWeatherDetails(latitude,longitude,apikey)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {

                    Log.d(TAG,"Response is succesfull")
                    currentWeather = response.body()!!
                    _location.value = location
                    _temp.value = currentWeather.current.temp.toString() + " \u2103"

                    if(currentWeather.current.weather.isNotEmpty()) {
                        weatherCondition = currentWeather.current.weather[0]
                        _weatherDescription.value = weatherCondition.description
                        _weatherIconUrl.value =imageBaseUrl + weatherCondition.icon + imageExtension
                    }

                } else {
                    Log.d(TAG,"Response is not successfull")
                }
            }
        }
    }

    /**
     * Gets current weather details of particular location
     */
    fun getSearchLocationWeather(location : String) {
        if(!location.isNullOrEmpty()) {
            var latitude  = 0.0
            var longitude  = 0.0
            var searchLocation = ""
            var addressList = (geocoder.getFromLocationName(location,1) as List<Address>)
            if( !addressList.isNullOrEmpty()) {
                searchLocation = addressList[0].locality
                latitude = addressList[0].latitude
                longitude = addressList[0].longitude
                getWeatherDetails(latitude,longitude,searchLocation)
            }
        }
    }

    /**
     * Gets current weather details of office location
     */
    fun getCurrentOfficeWeather() {
        val currentLatitiude = 52.364138
        val currentLongitude = 4.891697
        var officeLocation  = ""
        var addressList  = (geocoder.getFromLocation(currentLatitiude,currentLongitude,1) as List<Address>)
        if( !addressList.isNullOrEmpty()) {
            officeLocation = addressList[0].locality
            getWeatherDetails(currentLatitiude, currentLongitude,officeLocation)
        }
    }

    // Binding adapter to load the image using glide
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view).load(url).into(view)
            }
        }
    }

}