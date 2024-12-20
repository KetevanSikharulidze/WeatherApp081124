package com.example.weatherapp081124.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp081124.MainViewModel
import com.example.weatherapp081124.R
import com.example.weatherapp081124.adapters.VpAdapter
import com.example.weatherapp081124.databinding.FragmentMainBinding
import com.example.weatherapp081124.models.WeatherItem
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY = "62c45b028a7f491d87864226241612"

class MainFragment : Fragment() {

    private lateinit var pLauncher: ActivityResultLauncher<String>

    private lateinit var binding: FragmentMainBinding

    private val model : MainViewModel by activityViewModels()

    private val fList = listOf(
        HoursFragment.newInstance(), DaysFragment.newInstance()
    )

    private val tList = listOf(
        "HOURS", "DAYS"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()
        requestWeatherData("Paris")
    }

    private fun init() = with(binding){
        val adapter = VpAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout,vp){
            tab, position -> tab.text = tList[position]
        }.attach()
    }

    private fun updateCurrentCard() = with(binding){
        model.liveDataCurrent.observe(viewLifecycleOwner){
            val maxMinTemp ="${it.maxTemp}°C/${it.minTemp}°C"
            tvCity.text = it.city
            tvDate.text = it.time
            tvTempCurrent.text = it.tempCurrent
            tvCondition.text = it.condition
            tvMinMaxTemp.text = maxMinTemp
            Picasso.get().load("https:" + it.conditionURL).into(imCondition)
        }
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherItem){
        val item = WeatherItem(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.minTemp,
            weatherItem.maxTemp,
            weatherItem.hours
        )
        model.liveDataCurrent.value = item
//        Log.d("MyLog","City: ${item.city}")
//        Log.d("MyLog","time: ${item.time}")
//        Log.d("MyLog","temp: ${item.tempCurrent}")
//        Log.d("MyLog","condition: ${item.condition}")
//        Log.d("MyLog","url: ${item.conditionURL}")
        Log.d("MyLog","city: ${item.city}")
        Log.d("MyLog","minTemp: ${item.minTemp}")
        Log.d("MyLog","maxTemp: ${item.maxTemp}")
        Log.d("MyLog","hours: ${item.hours}")
    }

    private fun parseDays(mainObject: JSONObject): List<WeatherItem>{
        val list = ArrayList<WeatherItem>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()){
            val day = daysArray[i] as JSONObject
            val item = WeatherItem(
                name,
                day.getString("date"),
                "",
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        return list
    }

    private fun parseWeatherData(result: String){
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)
        parseCurrentData(mainObject, list[0])
    }

    private fun requestWeatherData(city: String){
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "8" +
                "&aqi=no&alerts=no\n"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parseWeatherData(result)
            },
            {
                error -> Log.d("MyLog","Error: $error")
            }
        )
        queue.add(request)
    }

    private fun permissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            Log.d("MyLog", "permission is $it") // შეგვიძლია არც არაფერი გამოვიძახოთ, ლოგირება დაგვეხმარება შევამოწმოთ მიცემულია თუ არა წვდომა
            Toast.makeText(activity, "permission is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(){
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}