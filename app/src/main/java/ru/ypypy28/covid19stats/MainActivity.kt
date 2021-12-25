package ru.ypypy28.covid19stats

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity: AppCompatActivity() {

    lateinit var appViewModel: ApplicationViewModel
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null

    // maybe move to mainViewModel when this section will become too large
    private lateinit var noCountryLocationData: String
    private lateinit var dontAskForPermissionAgain: String
    private lateinit var permissionRefused: String
    private lateinit var permissionDenied: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noCountryLocationData = resources.getString(R.string.no_country_location_data)
        dontAskForPermissionAgain = resources.getString(R.string.dont_ask_for_permission_again)
        permissionRefused = resources.getString(R.string.permission_refused)
        permissionDenied = resources.getString(R.string.permission_denied)

        appViewModel = ViewModelProvider(this).get(ApplicationViewModel::class.java)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as? NavHostFragment ?: return
        navController = navHostFragment!!.navController

        val mainBottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        mainBottomNavView.setupWithNavController(navController)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onStart() {
        super.onStart()

        if (!checkLocationPermission()) requestPermission()
        getLocation()
//        navigateFromCountriesToCities()
    }

    fun navigateFromCountriesToCities() {
        navController.navigate(R.id.action_nav_countries_to_nav_list_cities)
    }

    fun navigateFromCitiesToCountries() {
        navController.navigate(R.id.action_nav_list_cities_to_nav_countries)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result

                Log.d("LOCATION", "${location.latitude}, ${location.longitude}")

                try {
                    val country = getCountryFromCoordinates(location.latitude, location.longitude)
                    if (country == "United States") appViewModel.country.value = "US"
                    else appViewModel.country.value = country
                } catch (e: java.io.IOException) {
                    Log.d("LOCATION exception", e.message.toString())
                    Toast.makeText(this, noCountryLocationData, Toast.LENGTH_SHORT).show()
                    navigateFromCitiesToCountries()
                }

            } else {
                Toast.makeText(this, noCountryLocationData, Toast.LENGTH_SHORT).show()
                Log.d("LOCATION",  "NO LOCATION")
                navigateFromCitiesToCountries()
            }
        }
    }

    private fun getCountryFromCoordinates(lat: Double, lon: Double): String {

        // set Locale to US because remote API sends name of Country in english
        val geocoder = Geocoder(this, Locale.US)
        val adresses = geocoder.getFromLocation(lat, lon, 1)
        return adresses[0].countryName

    }

    private fun checkLocationPermission(): Boolean {
        val state = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return state == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_CODE)
    }

    private fun requestPermission() {
        val dontAskMeAgain = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if (dontAskMeAgain) {
            //тут мы можем попросить пользователя, не будет
            //ли он против если мы ему покажем запрос на разрешение
            Toast.makeText(this, dontAskForPermissionAgain, Toast.LENGTH_SHORT).show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.size <= 0) {
                Toast.makeText(this, permissionRefused, Toast.LENGTH_SHORT).show()
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, permissionDenied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val REQUEST_CODE = 13
    }

}