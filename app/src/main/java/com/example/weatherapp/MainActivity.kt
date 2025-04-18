package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationUtils: LocationUtils
    private lateinit var weatherInfoUtils: WeatherInfoUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        locationUtils = LocationUtils(this) {
            if (it) {
                getCurrentLocationAndFetchWeather()
            } else {
                Toast.makeText(this, "please provide location permission", Toast.LENGTH_SHORT).show()
            }
        }

        locationUtils.requestLocationPermissions()
        weatherInfoUtils = WeatherInfoUtils(this)

        binding.submitButton.setOnClickListener {
            getCurrentLocationAndFetchWeather()
        }
    }

    private fun getCurrentLocationAndFetchWeather() {
        locationUtils.getCurrentLocationAndFetchWeather { location ->

            if (location != null) {
                weatherInfoUtils.fetchWeather(location.latitude, location.longitude) {
                    if (it != null) {
                        updateUI(it)
                    } else {
                        Toast.makeText(this, "Failed to fetch weather", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Failed to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(weatherResponse: WeatherResponse) {
        val weatherInfo = "Temperature: ${weatherResponse.main.temp}°C\n" +
                "Feels Like: ${weatherResponse.main.feels_like}°C\n" +
                "Humidity: ${weatherResponse.main.humidity}%\n" +
                "Description: ${weatherResponse.weather.firstOrNull()?.description ?: "N/A"}\n" +
                "City: ${weatherResponse.name}"
        binding.weatherInfoTextView.text = weatherInfo
    }
}