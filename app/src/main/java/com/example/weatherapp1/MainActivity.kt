package com.example.weatherapp1

import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import com.example.weatherapp1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
//   api key:  e702742537b2e1061754bd5636b0ded7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    fetchWeatherData("Jaipur")
    searchCity()
    }

    private fun searchCity() {
      val searchview=binding.searchView
        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
    }

    private fun fetchWeatherData( cityname:String) {
     val retrofit=Retrofit.Builder()
         .addConverterFactory(GsonConverterFactory.create())
         .baseUrl("https://api.openweathermap.org/data/2.5/")
         .build().create(ApiInterface::class.java)


        val response = retrofit.getWeatherData(cityname, "e702742537b2e1061754bd5636b0ded7", "metric")

        response.enqueue(object:Callback<weatherapp>{
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody=response.body()
                if(response.isSuccessful && responseBody !=null){
                    val tempretur=responseBody.main.temp.toString()
                    val Humidity=responseBody.main.humidity
                    val windSpeed=responseBody.wind.speed
                    val sunRise=responseBody.sys.sunrise.toLong()
                    val sunSet=responseBody.sys.sunset.toLong()
                    val seaLevel=responseBody.main.pressure
                    val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp=responseBody.main.temp_max
                    val minTemp=responseBody.main.temp_min



                  binding.temp.text="$tempretur °C"
                  binding.weather.text=condition
                  binding.maxtemp.text="Max Temp: $maxTemp °C "
                  binding.mintemp.text="Min Temp: $minTemp °C "
                  binding.humidity.text="$Humidity"
                    binding.windSpeed.text="$windSpeed m/s"
                    binding.sunrise.text="${time(sunRise)}"
                    binding.sunset.text="${time(sunSet)}"
                    binding.sea.text="$seaLevel hPa"
                    binding.conditions.text=condition
                    binding.day.text=dayName(System.currentTimeMillis())
                        binding.date.text=date()
                    binding.cityname.text="$cityname"

                    changeImageAccordingToWeatherCondition(condition)


                }

            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {

            }


        })



    }

    private fun changeImageAccordingToWeatherCondition(Conditions:String) {
       when(Conditions){
           "Clear Sky","Sunny","Clear","Haze" -> {
               binding.root.setBackgroundResource(R.drawable.sunny_background)
               binding.lottieAnimationView.setAnimation(R.raw.sun)
           }
           "Partly Clouds","Clouds","Overcast","mist","Foggy" -> {
               binding.root.setBackgroundResource(R.drawable.colud_background)
               binding.lottieAnimationView.setAnimation(R.raw.cloud)
           }
           "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" -> {
               binding.root.setBackgroundResource(R.drawable.rain_background)
               binding.lottieAnimationView.setAnimation(R.raw.rain)
           }
           "Light Snow","Moderate Snow","Heavy Snow","Blizzard" -> {
               binding.root.setBackgroundResource(R.drawable.snow_background)
               binding.lottieAnimationView.setAnimation(R.raw.snow)
           }
           else->{
               binding.root.setBackgroundResource(R.drawable.sunny_background)
               binding.lottieAnimationView.setAnimation(R.raw.sun)
           }
       }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date():String{
        val sdf=SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
   private fun dayName(timestamp:Long):String{
        val sdf=SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
   private fun time(timestamp:Long):String{
        val sdf=SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

}