package science.involta.covid19statsdyachkov.data

import androidx.room.Database
import androidx.room.RoomDatabase
import science.involta.covid19statsdyachkov.data.models.City
import science.involta.covid19statsdyachkov.data.models.FavoriteCountry

@Database(entities=arrayOf(City::class, FavoriteCountry::class), version=1)
abstract class CityDatabase: RoomDatabase() {

    abstract fun localDAO(): ILocalDAO
}