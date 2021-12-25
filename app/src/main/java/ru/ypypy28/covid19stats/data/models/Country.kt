package ru.ypypy28.covid19stats.data.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Country(@SerializedName("country")
                   @ColumnInfo(name = "country")
                   var name: String,
                   var confirmed: Int?,
                   var deaths: Int?,
                   var recovered: Int?,
                   var isFavorite: Boolean = false)
