package science.involta.covid19statsdyachkov.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import science.involta.covid19statsdyachkov.data.models.City
import science.involta.covid19statsdyachkov.data.models.Country
import science.involta.covid19statsdyachkov.data.models.FavoriteCountry
import science.involta.covid19statsdyachkov.net.RetrofitClientInstance
import science.involta.covid19statsdyachkov.net.api.ICovid19APIService
import science.involta.covid19statsdyachkov.net.api.helper.ResponseObj

class Repository(application: Application) {
    private val app = application
    private val remoteAPI = RetrofitClientInstance
        .retrofitInstance?.create(ICovid19APIService::class.java)!!
    private val localAPI = Room.databaseBuilder(app, CityDatabase::class.java, "covidStats")
        .build().localDAO()

    fun fetchCities(countryName: String? = null): LiveData<ArrayList<City>> {
        val _cities = MutableLiveData<ArrayList<City>>()
        val call: Call<ResponseObj>?
        if (countryName != null) {
            call = remoteAPI.getProvincesOf(countryName)
        } else {
            call = remoteAPI.getAllProvinces()
        }
        Log.d("Repository", "in Repository")
        call.enqueue(object: Callback<ResponseObj> {
            override fun onResponse(
                call: Call<ResponseObj>,
                response: Response<ResponseObj>
            ) {
                _cities.value = response.body()?.data?.covid19Stats
            }

            override fun onFailure(call: Call<ResponseObj>, t: Throwable) {
                Log.d("Repository", "Failure ${t.message}")
            }

        })

        return _cities
    }

    suspend fun updateLocalCities(cities: ArrayList<City>?) = withContext(Dispatchers.IO){
        try {
            localAPI.insertAllCities(cities!!)}
        catch (e: Exception) {
            Log.d("UpdateLocalCities Error", e.message.toString() )
        }
     }

    fun getAllCountries(): LiveData<List<Country>> {
        return localAPI.getAllCountries()
    }

    fun getProvincesOf(countryName: String): LiveData<List<City>> {
        return localAPI.getCitiesOf(countryName)
    }

    fun getFavoriteCountries(): LiveData<List<Country>> {
        return localAPI.getFavoriteCountries()
    }

    suspend fun addCountryToFavorites(country: Country) = withContext(Dispatchers.IO) {
        localAPI.addToFavorites(FavoriteCountry(country.name))
    }

    suspend fun removeCountryFromFavorites(country: Country) = withContext(Dispatchers.IO) {
        localAPI.removeFromFavorites(FavoriteCountry(country.name))
    }
}