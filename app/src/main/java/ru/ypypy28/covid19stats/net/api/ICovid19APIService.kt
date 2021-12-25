package ru.ypypy28.covid19stats.net.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ypypy28.covid19stats.net.api.helper.ResponseObj

interface ICovid19APIService {

    @GET("https://covid19-api.weedmark.systems/api/v1/stats")
    fun getAllProvinces(): Call<ResponseObj>

    @GET("https://covid19-api.weedmark.systems/api/v1/stats")
    fun getProvincesOf(
        @Query("country") countryName: String
    ): Call<ResponseObj>
}