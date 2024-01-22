package hr.gregl.goldenhourphotography.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.DATA_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.adapter.ItemAdapter
import hr.gregl.goldenhourphotography.api.TimeFetcher
import hr.gregl.goldenhourphotography.api.WeatherFetchListener
import hr.gregl.goldenhourphotography.api.WeatherFetcher
import hr.gregl.goldenhourphotography.databinding.FragmentItemsBinding
import hr.gregl.goldenhourphotography.framework.fetchItems
import hr.gregl.goldenhourphotography.handler.DateHandler
import hr.gregl.goldenhourphotography.model.Item
import hr.gregl.goldenhourphotography.util.LocationData
import hr.gregl.goldenhourphotography.util.LocationUtils


class ItemsFragment : Fragment() {

    private lateinit var items: MutableList<Item>
    private lateinit var binding: FragmentItemsBinding
    private lateinit var itemAdapter: ItemAdapter
    private val dateHandler = DateHandler()

    private var weatherDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        itemAdapter = ItemAdapter(requireContext(), items)
        initWeatherDialog()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), items)
        }

        binding.btnGetSunrise.setOnClickListener {
            handleGetSunriseClick()
        }

        binding.btnGetCurrentLocation.setOnClickListener {
            handleGetCurrentLocationClick()
        }

        setTimezoneText(view)

        Handler(Looper.getMainLooper()).postDelayed({
            showGetWeatherPopup()
        }, 1000)

    }

    private fun initWeatherDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)
        builder.setView(dialogView)

        val messageTextView = dialogView.findViewById<TextView>(R.id.tvMessage)
        messageTextView.text = getString(R.string.would_you_like_to_fetch_the_latest_weather_data)

        val okButton = dialogView.findViewById<Button>(R.id.btnOk)
        okButton.setOnClickListener {
            fetchAndDisplayWeatherData()
            weatherDialog?.dismiss()
        }

        val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel)
        cancelButton.setOnClickListener {
            weatherDialog?.dismiss()
        }

        weatherDialog = builder.create()
    }

    @SuppressLint("SetTextI18n")
    private fun setTimezoneText(view: View) {
        val tvCurrentTimeZone = view.findViewById<TextView>(R.id.tvCurrentTimeZone)
        if (items.isNotEmpty()) {
            val timeZone = items[0].timezone?.substringAfter("/") ?: "Unknown"
            tvCurrentTimeZone.text = "Time zone: $timeZone"
        } else {
            tvCurrentTimeZone.text = "Time zone: Not available"
        }
    }

    private fun handleGetSunriseClick() {
        val latitude = binding.etLatitude.text.toString().toDoubleOrNull()
        val longitude = binding.etLongitude.text.toString().toDoubleOrNull()

        LocationData.setLatitude(latitude)
        LocationData.setLongitude(longitude)

        if (latitude != null && longitude != null) {
            fetchAndDisplayItems(latitude = latitude, longitude = longitude)
        } else {
            Toast.makeText(
                requireContext(),
                "Invalid latitude or longitude",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleGetCurrentLocationClick() {
        if (LocationUtils.hasLocationPermission(requireContext())) {
            if (LocationUtils.isLocationEnabled(requireContext())) {
                LocationUtils.getCurrentLocation(requireContext())
                updateEditTextFields()
            } else {
                LocationUtils.promptUserToEnableLocationIfNeeded(requireActivity())
            }
        } else {
            LocationUtils.checkLocationPermission(requireActivity())
        }
    }

    private fun updateEditTextFields() {
        Handler(Looper.getMainLooper()).postDelayed({
            val currentLatitude = LocationData.getLatitude()
            val currentLongitude = LocationData.getLongitude()

            binding.etLatitude.setText(currentLatitude.toString())
            binding.etLongitude.setText(currentLongitude.toString())
        }, 500)
    }


    private fun fetchAndDisplayItems(latitude: Double, longitude: Double) {
        clearOldData()

        val (startDate, endDate) = dateHandler.getStartAndEndDates()

        TimeFetcher(requireContext()).fetchItems(latitude, longitude, startDate, endDate)

        refreshItems()
    }


    private fun clearOldData() {
        requireContext().contentResolver.delete(DATA_PROVIDER_CONTENT_URI, null, null)
        items.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshItems() {
        items.clear()
        items.addAll(requireContext().fetchItems())
        itemAdapter.notifyDataSetChanged()
        binding.rvItems.adapter?.notifyDataSetChanged()
    }


    private fun showGetWeatherPopup() {
        weatherDialog?.show()
    }

    private fun fetchAndDisplayWeatherData() {
        WeatherFetcher(requireContext(), object : WeatherFetchListener {
            override fun onWeatherDataFetched() {
                refreshItems()
            }
        }).fetchWeather(
            LocationData.getLatitude(),
            LocationData.getLongitude()
        )
    }


    private val timeUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            refreshItems()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(TimeFetcher.ACTION_DATA_UPDATED)
        requireContext().registerReceiver(timeUpdateReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(timeUpdateReceiver)
    }

}
