package ru.ypypy28.covid19stats.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites",
//        foreignKeys = arrayOf(
//            ForeignKey(
//                entity = City::class,
//                parentColumns = arrayOf("country"),
//                childColumns = arrayOf("country"),
//                onDelete = ForeignKey.CASCADE
//            )),
//    indices = arrayOf(Index(value = arrayOf("country"), unique = true))
)
data class FavoriteCountry(@PrimaryKey
                           val country: String,
                           )
