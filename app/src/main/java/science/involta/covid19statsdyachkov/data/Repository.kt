package science.involta.covid19statsdyachkov.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import kotlinx.coroutines.*
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

    fun fetchData(scope: CoroutineScope, countryName: String? = null) {
        val call: Call<ResponseObj>?
        if (countryName != null) {
            call = remoteAPI.getProvincesOf(countryName)
        } else {
            call = remoteAPI.getAllProvinces()
        }

        call.enqueue(object: Callback<ResponseObj> {
            override fun onResponse(
                call: Call<ResponseObj>,
                response: Response<ResponseObj>
            ) {
                scope.launch {
                    updateLocalCities(response.body()?.data?.covid19Stats)
                }
            }

            override fun onFailure(call: Call<ResponseObj>, t: Throwable) {
                Log.d("Repository", "Failure ${t.message}")
            }

        })

    }

    suspend fun updateLocalCities(cities: List<City>?) = withContext(Dispatchers.IO){
        try {
            localAPI.insertAllCities(cities!!)}
        catch (e: Exception) {
            Log.d("UpdateLocalCities Error", e.message.toString() )
        }
     }

    fun getAllCountries(): LiveData<List<Country>> {
        return localAPI.getAllCountries()
    }

    fun getCitiesOf(countryName: String): LiveData<List<City>> {
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