package com.bangkit.relaverse.ui.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
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
            /* Test Logout */
            tvCurrentLocation.setOnClickListener {
                viewModel.logout()
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
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
        Log.d("Tken", token)
        viewModel.campaignResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.items.let { hasil ->
                        campaignAdapter.submitList(hasil)
                        Log.d("berhasil", hasil.toString())
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }


        }
    }

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
                                            Toast.makeText(
                                                context, it.data.message, Toast.LENGTH_LONG
                                            ).show()

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

                    Log.d("longlat", latLng.longitude.toString())
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
        campaignAdapter = CampaignAdapter(requireContext())
        binding.apply {
            rvCampaign.layoutManager = LinearLayoutManager(requireContext())
            rvCampaign.setHasFixedSize(true)
            rvCampaign.adapter = campaignAdapter

        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}