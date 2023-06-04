package com.bangkit.relaverse.ui.main.profile

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.relaverse.data.remote.response.User
import com.bangkit.relaverse.data.utils.Resource
import com.bangkit.relaverse.databinding.FragmentProfileBinding
import com.bangkit.relaverse.ui.ViewModelFactory
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        loadProfile()
        return binding.root
    }

    private fun loadProfile() {
        viewModel.apply {
            getToken().observe(requireActivity()) { token ->
                getUserId().observe(requireActivity()) { userId ->
                    if (token != null && userId != null) {
                        getUserById(token, userId.toInt())
                    }
                }
            }

            profileResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showToast("Error")
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        showToast(result.data.message)
                        showProfile(result.data.user)
                    }
                }
            }
        }
    }

    private fun showProfile(user: User) {
        binding.apply {
            nameEditText.setText(user.name)
            emailEditText.setText(user.email)
            phoneEditText.setText(user.phoneNumber)
            addressEditText.setText(
                latLngToAddress(user.latitude!!, user.longitude!!)
            )
        }
    }

    private fun latLngToAddress(lat: Float, lon: Float): String {
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())

        val address = geocoder.getFromLocation(
            lat.toDouble(),
            lon.toDouble(),
            1
        )
        return address!![0].locality.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            else -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}