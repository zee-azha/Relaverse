package com.bangkit.relaverse.ui.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.remote.response.CampaignData
import com.bangkit.relaverse.data.utils.CampaignAdapter
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.FragmentHomeBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.auth.WelcomeActivity
import com.bangkit.relaverse.ui.main.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    var token: String = ""
    var id: String = ""
    private lateinit var campaignAdapter: CampaignAdapter
    private var job: Job = Job()
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            launch {
                viewModel.getToken().collect { tokenID ->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
            launch {
                viewModel.getId().collect { UID ->
                    if (!UID.isNullOrEmpty()) id = UID
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.logout()
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finishAffinity()

            }
            refreshLocation.setOnClickListener {
                getLocation()
                getCampaign()
            }
        }
        getCampaign()
        setAdapter()
        getLocation()
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLocation()
        }
    }

    private fun getCampaign() {
        viewModel.getAllCampaign(token)
        viewModel.campaignResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    showLoading(false)
                    result.data.items.let { data ->
                        campaignAdapter.submitList(data)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    binding.apply {
                        lifecycleScope.launch {
                            if (job.isActive) job.cancel()
                            job = launch {
                                viewModel.updateLocation(
                                    token,
                                    id.toInt(),
                                    latLng.latitude.toString(),
                                    latLng.longitude.toString()
                                ).collect {
                                    when (it) {
                                        is Resource.Success -> {
                                            showLoading(false)
                                            geocoder =
                                                Geocoder(requireContext(), Locale.getDefault())
                                            val address = geocoder.getFromLocation(
                                                latLng.latitude, latLng.longitude, 1
                                            )
                                            tvCurrentLocation.text =
                                                address?.get(0)!!.locality.toString()
                                        }

                                        is Resource.Error -> {
                                            showLoading(false)
                                            Toast.makeText(
                                                context, it.error.toString(), Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        is Resource.Loading -> {
                                            showLoading(true)

                                        }
                                    }
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_activate_location_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setAdapter() {
        campaignAdapter = CampaignAdapter()
        binding.apply {
            rvCampaign.layoutManager = LinearLayoutManager(requireContext())
            rvCampaign.setHasFixedSize(true)
            rvCampaign.adapter = campaignAdapter

        }

        campaignAdapter.setOnItemClickCallback(object : CampaignAdapter.OnItemClickCallback {
            override fun onItemClicked(campaignData: CampaignData) {
                val intent = Intent(context, DetailsHomeActivity::class.java)
                intent.putExtra(DetailsHomeActivity.CAMPAIGN_ID, campaignData.id)
                startActivity(intent)
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}