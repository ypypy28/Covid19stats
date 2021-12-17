package science.involta.covid19statsdyachkov.data

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Country(@SerializedName("country")
                   @ColumnInfo(name = "country")
                   var name: String,
                   var confirmed: Int?,
                   var deaths: Int?,
                   var recovered: Int?)
