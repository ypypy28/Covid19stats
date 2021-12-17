package science.involta.covid19statsdyachkov.net.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import science.involta.covid19statsdyachkov.net.api.helper.ResponseObj

interface ICovid19APIService {

    @GET("https://covid19-api.weedmark.systems/api/v1/stats")
    fun getAllProvinces(): Call<ResponseObj>
//    fun getAllProvinces(): Call<ArrayList<Province>>

    @GET("https://covid19-api.weedmark.systems/api/v1/stats")
    fun getProvincesOf(
        @Query("country") countryName: String
    ): Call<ResponseObj>
}