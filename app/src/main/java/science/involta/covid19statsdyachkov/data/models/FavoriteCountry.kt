package science.involta.covid19statsdyachkov.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
