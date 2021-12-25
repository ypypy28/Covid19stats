package ru.ypypy28.covid19stats.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ypypy28.covid19stats.data.models.City
import ru.ypypy28.covid19stats.data.models.Country
import ru.ypypy28.covid19stats.data.models.FavoriteCountry
import ru.ypypy28.covid19stats.net.RetrofitClientInstance
import ru.ypypy28.covid19stats.net.api.ICovid19APIService
import ru.ypypy28.covid19stats.net.api.helper.ResponseObj

class Repository(application: Application) {
    private val app = application
    private val remoteAPI = RetrofitClientInstance
        .retrofitInstance?.create(ICovid19APIService::class.java)!!
    private val localAPI = Room.databaseBuilder(app, CityDatabase::class.java, "covidStats")
        .build().localDAO()

    fun fetchData(scope: CoroutineScope, countryName: String? = null) {
        scope.launch {
            while (true) {
                val call: Call<ResponseObj>?
                if (countryName != null) {
                    call = remoteAPI.getProvincesOf(countryName)
                } else {
                    call = remoteAPI.getAllProvinces()
                }

                call.enqueue(object : Callback<ResponseObj> {
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
                // Sleeping until the next data update in the API
                // API updates once in an hour
                delay(1000*60*60)
            }
        }
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