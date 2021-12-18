package science.involta.covid19statsdyachkov.net.api.helper

import science.involta.covid19statsdyachkov.data.models.City

data class ResponseObj(val error: Boolean,
                       val statusCode: Int,
                       val message: String,
                       val data: ResponseData) {

    data class ResponseData(val lastChecked: String,
                            val covid19Stats: ArrayList<City>)
}
