package science.involta.covid19statsdyachkov.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=arrayOf(Province::class), version=1)
abstract class ProvinceDatabase: RoomDatabase() {

    abstract fun localDAO(): ILocalDAO
}