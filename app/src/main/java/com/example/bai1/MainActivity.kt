package com.example.bai1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var addressHelper: AddressHelper
    private lateinit var spProvince: Spinner
    private lateinit var spDistrict: Spinner
    private lateinit var spWard: Spinner
    private lateinit var tvDob: TextView
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addressHelper = AddressHelper(resources)
        spProvince = findViewById(R.id.sp_phuong)
        spDistrict = findViewById(R.id.sp_quan)
        spWard = findViewById(R.id.sp_tinh)
        tvDob = findViewById(R.id.tv_dob)
        calendarView = findViewById(R.id.calendar_view)

        loadProvinces()

        tvDob.setOnClickListener {
            if (calendarView.visibility == View.GONE) {
                calendarView.visibility = View.VISIBLE
            } else {
                calendarView.visibility = View.GONE
            }
        }

        // Set up a listener for the CalendarView to get the selected date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Format and display the selected date
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            tvDob.text = "Date of Birth: $selectedDate"

            // Hide the CalendarView after selection
            calendarView.visibility = View.GONE
        }
    }

    private fun loadProvinces() {
        val provinces = addressHelper.getProvinces()
        spProvince.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, provinces)

        spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                loadDistricts(selectedProvince)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadDistricts(province: String) {
        val districts = addressHelper.getDistricts(province)
        spDistrict.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, districts)

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDistrict = districts[position]
                loadWards(province, selectedDistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadWards(province: String, district: String) {
        val wards = addressHelper.getWards(province, district)
        spWard.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, wards)
    }
}