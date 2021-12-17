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
import science.involta.covid19statsdyachkov.net.RetrofitClientInstance
import science.involta.covid19statsdyachkov.net.api.ICovid19APIService
import science.involta.covid19statsdyachkov.net.api.helper.ResponseObj

class ProvinceService(application: Application) {
    private val app = application
    private val remoteAPI = RetrofitClientInstance
        .retrofitInstance?.create(ICovid19APIService::class.java)!!
    private val localAPI = Room.databaseBuilder(app, ProvinceDatabase::class.java, "covidStats")
        .build().localDAO()

    fun fetchProvinces(countryName: String? = null): LiveData<ArrayList<Province>> {
        val _provinces = MutableLiveData<ArrayList<Province>>()
//        val service = RetrofitClientInstance.retrofitInstance?.create(ICovid19APIService::class.java)
        val call: Call<ResponseObj>?
        if (countryName != null) {
//            call = service?.getProvincesOf(countryName)
            call = remoteAPI.getProvincesOf(countryName)
        } else {
            call = remoteAPI.getAllProvinces()
        }
        Log.d("ProvinceService", "in ProvinceService")
//        call?.enqueue(object: Callback<ResponseObj> {
        call.enqueue(object: Callback<ResponseObj> {
            override fun onResponse(
                call: Call<ResponseObj>,
                response: Response<ResponseObj>
            ) {
                _provinces.value = response.body()?.data?.covid19Stats
            }

            override fun onFailure(call: Call<ResponseObj>, t: Throwable) {
                Log.d("ProvinceService", "Failure ${t.message}")
            }

        })

        return _provinces
    }

//    fun updateLocalProvinces(provinces: List<Province>) {
//        try {
//            val localDAO = getLocalDAO()
//            localDAO.insertAllProvinces(provinces)
//        } catch (e: Exception) {
//            Log.d("UpdateLocalProvinces Error", e.message.toString())
//        }
//    }

    suspend fun updateLocalProvinces(provinces: ArrayList<Province>?) = withContext(Dispatchers.IO){
        try {
//            val localDAO = getLocalDAO()
//            localDAO.insertAllProvinces(provinces!!)}
            localAPI.insertAllProvinces(provinces!!)}
        catch (e: Exception) {
            Log.d("UpdateLocalProvinces Error", e.message.toString() )
        }
     }

//    internal fun getLocalDAO(): ILocalDAO {
//        val db = Room.databaseBuilder(app, ProvinceDatabase::class.java, "covidStats").build()
//        val localDAO = db.localDAO()
//        return localDAO
//    }

    fun getAllCountries(): LiveData<List<Country>> {
//        val db = getLocalDAO()
//        return db.getAllCountries()
        return localAPI.getAllCountries()
    }

    fun getProvincesOf(countryName: String): LiveData<List<Province>> {
//        val db = getLocalDAO()
//        return db.getProvincesOf(countryName)
        return localAPI.getProvincesOf(countryName)
    }
}