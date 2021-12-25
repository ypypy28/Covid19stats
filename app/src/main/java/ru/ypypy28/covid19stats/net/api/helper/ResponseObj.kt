package ru.ypypy28.covid19stats.net.api.helper

import ru.ypypy28.covid19stats.data.models.City

data class ResponseObj(val error: Boolean,
                       val statusCode: Int,
                       val message: String,
                       val data: ResponseData) {

    data class ResponseData(val lastChecked: String,
                            val covid19Stats: ArrayList<City>)
}
