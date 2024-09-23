package com.example.intento1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etLatitude: EditText
    private lateinit var etLongitude: EditText
    private lateinit var btnFetchWeather: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private var weatherList: MutableList<WeatherResponse> = mutableListOf()
    private var isGetRequest: Boolean = true // Controlar si hacemos GET o POST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLatitude = findViewById(R.id.etLatitude)
        etLongitude = findViewById(R.id.etLongitude)
        btnFetchWeather = findViewById(R.id.btnFetchWeather)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter(weatherList)
        recyclerView.adapter = weatherAdapter

        btnFetchWeather.setOnClickListener {
            val latitude = etLatitude.text.toString().toDoubleOrNull()
            val longitude = etLongitude.text.toString().toDoubleOrNull()

            if (latitude == null || longitude == null) {
                Toast.makeText(this, "Por favor ingrese una latitud y longitud válidas", Toast.LENGTH_SHORT).show()
            } else {
                if (isGetRequest) {
                    // Realiza la solicitud GET
                    fetchWeatherData(latitude, longitude)
                } else {
                    // Realiza la solicitud POST
                    val weatherData = WeatherData(latitude, longitude, "Descripción del clima")
                    postWeatherData(weatherData)
                }
                isGetRequest = !isGetRequest // Cambiar entre GET y POST
            }
        }
    }

    // Método para la solicitud GET
    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = "cb7cdb6155d150992c3c4396675877aa"
                val weatherResponse = RetrofitInstance.api.getWeather(latitude, longitude, apiKey)

                withContext(Dispatchers.Main) {
                    weatherList.clear()
                    weatherList.add(weatherResponse)
                    weatherAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al recuperar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para la solicitud POST
    private fun postWeatherData(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = "cb7cdb6155d150992c3c4396675877aa"
                val response: Response<Void> = RetrofitInstance.api.postWeatherData(weatherData, apiKey)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Datos enviados correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al enviar datos: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
