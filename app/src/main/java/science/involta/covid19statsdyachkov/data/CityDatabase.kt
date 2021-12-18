package science.involta.covid19statsdyachkov.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=arrayOf(City::class, FavoriteCountry::class), version=1)
abstract class CityDatabase: RoomDatabase() {

    abstract fun localDAO(): ILocalDAO
}