package com.example.practika12_var4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Add_weather_activity : AppCompatActivity() {

    private var weatherList: MutableList<Weather> = mutableListOf()
    private lateinit var temperature: EditText
    private lateinit var city: EditText
    private lateinit var date: EditText
    private lateinit var month: EditText
    private lateinit var year: EditText
    private lateinit var button: Button
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weather)
        getContacts()

        temperature = findViewById(R.id.editText_temperature)
        city = findViewById(R.id.editText_city)
        date = findViewById(R.id.editText_date)
        month = findViewById(R.id.editText_month)
        year = findViewById(R.id.editText_year)
        button = findViewById(R.id.add_weather)

        index = intent.getIntExtra("num", -1)
        selectWeather(index)

        button.setOnClickListener {
            if (index == -1){
                addWeather(temperature.text.toString(), city.text.toString(), date.text.toString(), month.text.toString(), year.text.toString())
                Log.d("Hey", weatherList.toString())
            }
            else
            {
                updateWeather(temperature.text.toString(), city.text.toString(), date.text.toString(), month.text.toString(), year.text.toString())
            }
        }
    }

    private fun addWeather(temperature:String, city:String, date:String, month:String, year:String) {
        val weather = Weather(temperature, city,date,month,year)
        weatherList.add(weather)
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        preferences.edit {
            this.putString("json", Gson().toJson(weatherList).toString())
        }
    }

    private fun updateWeather(temperature:String, city:String, date:String, month:String, year:String){
        val contact = Weather(temperature, city,date,month,year)
        weatherList[index] = contact
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        preferences.edit {
            this.putString("json", Gson().toJson(weatherList).toString())
        }
    }

    private fun selectWeather(index: Int){
        if (index > -1){
            temperature.setText(weatherList[index].temperature)
            city.setText(weatherList[index].city)
            date.setText(weatherList[index].date)
            month.setText(weatherList[index].month)
            year.setText(weatherList[index].year)
            button.setText(R.string.update_weather)
        }
    }

    private fun getContacts() {
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        var json:String = ""
        if (!preferences.contains("json")){
            return
        }else{
            json = preferences.getString("json", "NOT_JSON").toString()
        }
        val tempList = Gson().fromJson<List<Weather>>(json, object: TypeToken<List<Weather>>(){}.type)
        weatherList.addAll(tempList)
    }

    fun back(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}