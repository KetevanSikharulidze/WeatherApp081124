package com.example.weatherapp081124.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp081124.R
import com.example.weatherapp081124.adapters.WeatherAdapter
import com.example.weatherapp081124.databinding.FragmentHoursBinding
import com.example.weatherapp081124.models.WeatherItem

class HoursFragment : Fragment() {

    private lateinit var binding : FragmentHoursBinding
    private lateinit var adapter : WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
    }

    private fun initRcView() = with(binding){
        rcViewHours.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcViewHours.adapter = adapter
        val list = listOf(
            WeatherItem("",
                "12:00",
                "5C",
                "Cloudy",
                "",
                "",
                "",
                ""),
            WeatherItem("",
                "13:00",
                "5C",
                "Cloudy",
                "",
                "",
                "",
                ""),
            WeatherItem("",
                "14:00",
                "5C",
                "Cloudy",
                "",
                "",
                "",
                ""),
            WeatherItem("",
                "15:00",
                "5C",
                "Cloudy",
                "",
                "",
                "",
                "")
        )
        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}