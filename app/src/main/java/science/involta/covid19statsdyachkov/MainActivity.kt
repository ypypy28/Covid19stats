package science.involta.covid19statsdyachkov

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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

    private var topTextView: TextView? = null
    var country: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel = ViewModelProvider(this).get(ApplicationViewModel::class.java)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as? NavHostFragment ?: return
        navController = navHostFragment!!.navController

        val mainBottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        mainBottomNavView.setupWithNavController(navController)

        topTextView = findViewById(R.id.straight_text_view)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

//        val provinces = viewModel.provinceService.fetchProvinces()
//        provinces.observeForever(){
//            Log.d("MAIN-PROVINCE", "size is ${it.size.toString()}")
//        }
    }

    override fun onStart() {
        super.onStart()

        if (!checkLocationPermission()) requestPermission()
        else {
            getLocation()
            navigateToProvinces()
        }
    }

    fun navigateToProvinces() {
        navController.navigate(R.id.action_nav_fulllist_to_nav_list_provinces2)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result

                val country = getCountryFromCoordinates(location.latitude, location.longitude)
                if (country == "United States") appViewModel.country.value = "US"
                else appViewModel.country.value = country
                topTextView?.text = "You are in ${appViewModel.country.value}"

                Log.d("LOCATION",
                 "${location.latitude.toString()}, ${location.longitude.toString()}")
            } else {
                Toast.makeText(this, "Нет данных о геолокации", Toast.LENGTH_SHORT).show()
                Log.d("LOCATION",  "NO LOCATION")
            }
        }
    }

    private fun getCountryFromCoordinates(lat: Double, lon: Double): String {

        val geocoder = Geocoder(this, Locale.getDefault())
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
            Toast.makeText(this, "Не запрашивать больше", Toast.LENGTH_SHORT).show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.size <= 0) {
                Toast.makeText(this, "Пользователь отклонил запрос", Toast.LENGTH_SHORT).show()
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Пользователь запретил использование геолокации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val REQUEST_CODE = 13
    }

}