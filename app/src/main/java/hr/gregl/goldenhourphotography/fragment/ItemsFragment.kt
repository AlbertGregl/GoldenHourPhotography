package hr.gregl.goldenhourphotography.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.adapter.ItemAdapter
import hr.gregl.goldenhourphotography.api.TimeFetcher
import hr.gregl.goldenhourphotography.databinding.FragmentItemsBinding
import hr.gregl.goldenhourphotography.framework.fetchItems
import hr.gregl.goldenhourphotography.model.Item


class ItemsFragment : Fragment() {

    private lateinit var items: MutableList<Item>
    private lateinit var binding: FragmentItemsBinding
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = requireContext().fetchItems()
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        itemAdapter = ItemAdapter(requireContext(), items)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), items)
        }

        binding.btnGetSunrise.setOnClickListener {
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
    }

    private fun fetchAndDisplayItems(latitude: Double, longitude: Double) {
        TimeFetcher(requireContext()).fetchItems(latitude, longitude)

        Handler(Looper.getMainLooper()).postDelayed({
            refreshItems()
        }, 1000)
    }

    private fun refreshItems() {
        items.clear()
        items.addAll(requireContext().fetchItems())
        items.sortByDescending { it.utcOffset }
        itemAdapter.notifyDataSetChanged()
    }

}
