package com.example.intento1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherList: List<WeatherResponse>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTemperature: TextView = itemView.findViewById(    R.id.tvTemperature)
        val tvHumidity: TextView = itemView.findViewById(R.id.tvHumidity)
        val tvPressure: TextView = itemView.findViewById(R.id.tvPressure)
        val tvWindSpeed: TextView = itemView.findViewById(R.id.tvWindSpeed)
        val tvClouds: TextView = itemView.findViewById(R.id.tvClouds)
        val tvSunrise: TextView = itemView.findViewById(R.id.tvSunrise)
        val tvSunset: TextView = itemView.findViewById(R.id.tvSunset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.tvTemperature.text = "Temperatura: ${weather.main.temp} °C"
        holder.tvHumidity.text = "Humedad: ${weather.main.humidity}%"
        holder.tvPressure.text = "Presión: ${weather.main.pressure} hPa"
        holder.tvWindSpeed.text = "Velocidad del viento: ${weather.wind.speed} m/s"
        holder.tvClouds.text = "Nubes: ${weather.clouds.all}%"
        holder.tvSunrise.text = "Amanecer: ${convertUnixToTime(weather.sys.sunrise)}"
        holder.tvSunset.text = "Atardecer: ${convertUnixToTime(weather.sys.sunset)}"
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    private fun convertUnixToTime(unixTime: Long): String {
        val date = java.util.Date(unixTime * 1000)
        val format = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
        return format.format(date)
    }
}