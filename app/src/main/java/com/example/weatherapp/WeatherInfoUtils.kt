package com.example.weatherapp

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherInfoUtils(val context: Context) {

    fun fetchWeather(latitude: Double, longitude: Double, callback: (WeatherResponse?) -> Unit ) {
        val apiKey = context.applicationContext.getString(R.string.open_weather_map_api_key)
        val call = RetrofitClient.instance.getCurrentWeather(latitude, longitude, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    callback.invoke(weatherResponse)
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }
}