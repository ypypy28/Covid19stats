package science.involta.covid19statsdyachkov.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// TODO CHACNGE TO CITIES
@Entity(tableName = "cities")
data class City(@SerializedName("city")
                @ColumnInfo(name = "city")
                var name: String?,
                @PrimaryKey
                var keyId: String,
                var province: String?,
                var country: String,
                var confirmed: Int?,
                var deaths: Int?,
                var recovered: Int?,
                var lastUpdate: String = "",
                    )
