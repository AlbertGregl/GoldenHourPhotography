package hr.gregl.goldenhourphotography.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.DATA_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.adapter.ItemAdapter
import hr.gregl.goldenhourphotography.api.TimeFetcher
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        itemAdapter = ItemAdapter(requireContext(), items)
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
        if (latitude != null && longitude != null) {
            fetchAndDisplayItems(latitude, longitude)
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
        }, 1000)
    }

    private fun fetchAndDisplayItems(latitude: Double, longitude: Double) {
        clearOldData()

        val (startDate, endDate) = dateHandler.getStartAndEndDates()
        TimeFetcher(requireContext()).fetchItems(latitude, longitude, startDate, endDate)

        Handler(Looper.getMainLooper()).postDelayed({
            refreshItems()
        }, 1000)
    }

    private fun clearOldData() {
        requireContext().contentResolver.delete(DATA_PROVIDER_CONTENT_URI, null, null)
        items.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshItems() {
        items.addAll(requireContext().fetchItems())
        itemAdapter.notifyDataSetChanged()
    }

}
