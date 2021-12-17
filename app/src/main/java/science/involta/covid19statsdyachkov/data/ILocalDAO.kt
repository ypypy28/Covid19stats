package science.involta.covid19statsdyachkov.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ILocalDAO {

    @Query("SELECT * FROM provinces")
    fun getAllProvinces(): LiveData<List<Province>>

    @Query("SELECT * FROM provinces WHERE country = :countryName")
    fun getProvincesOf(countryName: String): LiveData<List<Province>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProvinces(provinces: List<Province>)

    @Delete
    fun delete(province: Province)

    @Query("SELECT country, confirmed, deaths, recovered FROM provinces GROUP BY country")
    fun getAllCountries(): LiveData<List<Country>>

}