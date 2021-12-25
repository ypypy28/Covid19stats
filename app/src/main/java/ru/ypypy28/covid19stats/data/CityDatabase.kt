package ru.ypypy28.covid19stats.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ypypy28.covid19stats.data.models.City
import ru.ypypy28.covid19stats.data.models.FavoriteCountry

@Database(entities=arrayOf(City::class, FavoriteCountry::class), version=1)
abstract class CityDatabase: RoomDatabase() {

    abstract fun localDAO(): ILocalDAO
}