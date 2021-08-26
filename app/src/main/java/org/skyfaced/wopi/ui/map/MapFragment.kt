package org.skyfaced.wopi.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentMapBinding
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.ui.search.SearchFragment
import org.skyfaced.wopi.utils.extensions.lazySafetyNone
import org.skyfaced.wopi.utils.extensions.round
import timber.log.Timber

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private val args by navArgs<MapFragmentArgs>()

    private var _map: GoogleMap? = null
    private val map get() = requireNotNull(_map) { "Map isn't initialized" }

    private val locationClient by lazySafetyNone {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onLocationPermissionResult
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("$args")
        (childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment).apply {
            getMapAsync(::onMapReady)
        }

        binding {
            btnCurrentLocation.setOnClickListener {
                locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            btnConfirm.setOnClickListener {
                setFragmentResult(
                    requestKey = SearchFragment.KEY_REQUEST_MAP_SEARCH,
                    result = bundleOf(SearchFragment.KEY_LATT_LONG to map.cameraPosition.target.plain)
                )
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun onLocationPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    map.moveCamera(CameraUpdateFactory.newLatLng(
                        LatLng(location.latitude, location.longitude)
                    ))
                }
            }
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.lbl_notification)
                .setMessage(R.string.message_permission)
                .setPositiveButton(R.string.lbl_ok) { _, _ -> }
                .show()
        }
    }

    private fun onMapReady(googleMap: GoogleMap) {
        Timber.d("GoogleMap is ready")
        _map = googleMap
        with(map) {
            uiSettings.isRotateGesturesEnabled = false
            binding.btnCurrentLocation.isEnabled = true
            binding.btnConfirm.isEnabled = true

            val initialCameraPosition = args.lattLong?.toLatLng() ?: cameraPosition.target
            moveCamera(CameraUpdateFactory.newLatLng(initialCameraPosition))

            setOnCameraIdleListener {
                clear()
                addMarker(MarkerOptions().position(cameraPosition.target))
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater, container, false)
    }

    private val LatLng.plain get() = "${latitude.round()}, ${longitude.round()}"

    private fun String.toLatLng(): LatLng {
        val (latt, long) = split(',')
            .map { it.trim().toDouble() }
        return LatLng(latt, long)
    }
}