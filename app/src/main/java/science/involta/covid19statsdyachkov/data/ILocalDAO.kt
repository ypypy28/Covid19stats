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
    fun delete(city: City)

    @Query("SELECT country, confirmed, deaths, recovered FROM cities GROUP BY country")
    fun getAllCountries(): LiveData<List<Country>>

}