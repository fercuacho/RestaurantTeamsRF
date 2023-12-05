package com.example.restaurantteamsrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.restaurantteamsrf.R
import com.example.restaurantteamsrf.application.RestaurantTeamsApp
import com.example.restaurantteamsrf.data.TeamRepository
import com.example.restaurantteamsrf.data.remote.model.TeamDetailDto
import com.example.restaurantteamsrf.databinding.FragmentTeamDetailBinding
import com.example.restaurantteamsrf.model.Video
import com.example.restaurantteamsrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private const val TEAM_ID = "team_id"

class TeamDetailFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private var teamId: String? = null

    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TeamRepository

    //Para Google Maps
    private lateinit var map: GoogleMap

    //Latitud y Longitud
    private var lat = 0.00
    private var lon = 0.00

    private var nameRest = ""

    private var fineLocationPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            actionPermissionGranted()
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso para poder ubicar la posición del usuario en el mapa")
                    .setPositiveButton("Entendido"){ _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "El permiso de ubicación se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videos = ArrayList<Video>()
        /*
        arguments?.let {
            teamId = it.getString(TEAM_ID)
            Log.d(Constants.LOGTAG, getString(R.string.id_recibido) + {teamId})

            repository = (requireActivity().application as RestaurantTeamsApp).repository

            lifecycleScope.launch {

                teamId?.let { id ->
                    //val call: Call<TeamDetailDto> = repository.getTeamDetail(id)
                    val call: Call<TeamDetailDto> = repository.getTeamDetailApiary(id)
                    //val call: Call<TeamDetailDto> = repository.getTeamDetailApiary(id)

                    call.enqueue(object: Callback<TeamDetailDto> {
                        override fun onResponse(
                            call: Call<TeamDetailDto>,
                            response: Response<TeamDetailDto>
                        ) {

                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvTitle.text = response.body()?.name
                                nameRest = response.body()?.name.toString()
                                tvManager.text = getString(R.string.manager_del_equipo) + response.body()?.manager
                                tvCountry.text = getString(R.string.pa_s_del_equipo) + response.body()?.contry
                                tvLenght.text = getString(R.string.tama_o_del_equipo) +response.body()?.lenght
                                tvMainDish.text = getString(R.string.platillo_principal) + response.body()?.mainDish
                                lat = response.body()?.latitud?.toDouble() ?: 0.00
                                lon = response.body()?.longitud?.toDouble() ?: 0.00

                                val url = response.body()?.video
                                val video = Video(url.toString(), "","")
                                videos.add(video)
                                //vpVideos.adapter = VideosAdapter(videos)


                                vvVideo.setVideoPath(video.url)
                                vvVideo.setOnPreparedListener{ mediaplayer ->
                                    //pbVideo.visibility = View.GONE
                                    mediaplayer.start()
                                    val videoRatio: Float = mediaplayer.videoWidth / mediaplayer.videoHeight.toFloat()
                                    val screenRatio: Float = vvVideo.width / vvVideo.height.toFloat()

                                    val scale: Float = videoRatio / screenRatio

                                    if (scale >= 1){
                                        vvVideo.scaleX = scale
                                    }else{
                                        vvVideo.scaleY = 1f/scale
                                    }

//                                    //Para que cambie al completarse
//                                    vvVideo.setOnCompletionListener { mediaplayer ->
//                                        mediaplayer.start()
//                                    }

                                }

                                Glide.with(requireContext())
                                    .load(response.body()?.logo)
                                    .into(ivImage)
                            }

                        }

                        override fun onFailure(call: Call<TeamDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), getString(R.string.no_hay_conexi_n), Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el MapView
        val mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Otros códigos que puedas tener aquí
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding.map.onDestroy()
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(teamId: String) =
//            TeamDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(TEAM_ID, teamId)
//                }
//            }
//    }

    private fun actionPermissionGranted() {

    }

    private fun updateOrRequestPermissions() {
        //Revisando el permiso
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //Tenemos los permisos
            actionPermissionGranted()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    override fun onLocationChanged(p0: Location) {
    }

    private fun createMarker(){
        val coordinates = LatLng(lat, lon)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(nameRest)
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restau))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }

}