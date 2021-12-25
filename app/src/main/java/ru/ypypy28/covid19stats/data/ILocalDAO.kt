package ru.ypypy28.covid19stats.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.ypypy28.covid19stats.data.models.City
import ru.ypypy28.covid19stats.data.models.Country
import ru.ypypy28.covid19stats.data.models.FavoriteCountry

@Dao
interface ILocalDAO {

    @Query("SELECT * FROM cities")
    fun getAllCities(): LiveData<List<City>>

    @Query("SELECT * FROM cities WHERE country = :countryName")
    fun getCitiesOf(countryName: String): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCities(cities: List<City>)

    @Delete
    fun deleteOneCity(city: City)

    @Query("""
        SELECT
        c.country country, sum(c.confirmed) confirmed, sum(c.deaths) deaths, sum(c.recovered) recovered,
        f.country IS NOT NULL as isFavorite
        FROM cities c
        LEFT OUTER JOIN favorites f ON c.country = f.country
        GROUP BY c.country
    """)

    fun getAllCountries(): LiveData<List<Country>>

    @Query("""
        SELECT
        c.country country, sum(c.confirmed) confirmed, sum(c.deaths) deaths, sum(c.recovered) recovered,
        f.country IS NOT NULL as isFavorite
        FROM cities c
        JOIN favorites f ON c.country = f.country
        GROUP BY c.country
    """)
    fun getFavoriteCountries(): LiveData<List<Country>>

    @Insert
    fun addToFavorites(country: FavoriteCountry)

    @Delete
    fun removeFromFavorites(country: FavoriteCountry)
}