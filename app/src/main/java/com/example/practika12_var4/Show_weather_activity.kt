package com.example.practika12_var4

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Show_weather_activity : AppCompatActivity() {

    private var contactList: MutableList<Weather> = mutableListOf()
    private lateinit var rv: RecyclerView
    var indexChanged = -1
    private lateinit var adapter: WeatherRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_weather_activity)
        getWeather()

        adapter = WeatherRVAdapter(this, contactList)
        val rvListener = object : WeatherRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@Show_weather_activity, Add_weather_activity::class.java)
                intent.putExtra("num", position)
                indexChanged = position
                startActivity(intent)
                Toast.makeText(this@Show_weather_activity, "Вы выбрали $position вкладку", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setClickListener(rvListener)
        rv = findViewById(R.id.recyclerView)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun getWeather() {
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        var json: String = ""
        if (!preferences.contains("json")){
            return
        }else{
            json = preferences.getString("json", "NOT_JSON").toString()
        }
        contactList = Gson().fromJson<MutableList<Weather>>(json, object : TypeToken<MutableList<Weather>>(){}.type)
    }
    fun back(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (indexChanged != -1){
            contactList.clear()
            getWeather()
            rv.adapter?.notifyDataSetChanged()
        }
    }
}