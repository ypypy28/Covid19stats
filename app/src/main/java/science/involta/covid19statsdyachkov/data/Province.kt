package science.involta.covid19statsdyachkov.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// TODO CHACNGE TO CITIES
@Entity(tableName = "provinces")
data class Province(@SerializedName("province")
                    @ColumnInfo(name = "province")
                    var name: String?,
                    @PrimaryKey
                    var keyId: String,
                    var country: String,
                    var confirmed: Int?,
                    var deaths: Int?,
                    var recovered: Int?,
                    var lastUpdate: String = "",
                    )
