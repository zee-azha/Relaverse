package com.bangkit.relaverse.ui.main.home

import android.Manifest
import android.app.Activity
import android.app.UiAutomation
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.relaverse.R
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.FragmentHomeBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import com.bangkit.relaverse.ui.auth.AuthViewModel
import com.bangkit.relaverse.ui.auth.LoginActivity
import com.bangkit.relaverse.ui.auth.WelcomeActivity
import com.bangkit.relaverse.ui.main.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    var token : String =""
    var id : String= ""
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
                viewModel.getToken().collect { tokenID->
                    if (!tokenID.isNullOrEmpty()) token = tokenID
                }
            }
            launch {
                viewModel.getId().collect{ UID ->
                    if (!UID.isNullOrEmpty()) id= UID
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Test Logout */

        getLocation()
        binding.tvCurrentLocation.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            //remove backstack
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLocation()
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if(location != null){
                    val latLng = LatLng(location.latitude, location.longitude)
                    binding.apply {

                        lifecycleScope.launch {
                            if (job.isActive) job.cancel()
                            job = launch {
                                viewModel.updateLocation(token,id.toInt(),latLng.latitude.toString(),latLng.longitude.toString()).collect {
                                    when (it) {
                                        is Resource.Success -> {
                                            Toast.makeText(
                                                context, it.data!!.message, Toast.LENGTH_LONG
                                            ).show()

                                        }

                                        is Resource.Error -> {
                                            Toast.makeText(
                                                context,
                                                it.error.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        is Resource.Loading -> {

                                        }
                                    }


                                }
                            }
                        }


                    }

                    Log.d("longlat" , latLng.longitude.toString())
                } else {
                    Toast.makeText(requireContext(), getString(R.string.please_activate_location_message), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

//    fun getLastLocation(){
//        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101)
//        }
//        val lastLocation = fusedLocationClient.lastLocation
//        lastLocation.addOnSuccessListener {
//            geocoder = Geocoder(requireContext(),Locale.getDefault())
//            if (it != null){
//                Log.d("alamat",it.latitude.toString())
//                val address = geocoder.getFromLocation(it.latitude,it.longitude,1)
//                Log.d("lengkap",address?.get(0)!!.countryName)
//
//            }
//        }
//        lastLocation.addOnFailureListener{
//
//        }
//    }


}