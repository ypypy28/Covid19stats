package science.involta.covid19statsdyachkov.data

import androidx.lifecycle.LiveData
import androidx.room.*

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