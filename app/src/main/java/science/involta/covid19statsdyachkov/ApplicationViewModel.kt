package science.involta.covid19statsdyachkov

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import science.involta.covid19statsdyachkov.data.Country
import science.involta.covid19statsdyachkov.data.Province
import science.involta.covid19statsdyachkov.data.ProvinceService

class ApplicationViewModel(app: Application): AndroidViewModel(app) {
    private var provinceService: ProvinceService = ProvinceService(app)
    val country = MutableLiveData<String>()
    var countries: LiveData<List<Country>>
//    var country: String? = "Russia" // dirty hack
//    var provinces: LiveData<List<Province>> = Transformations.switchMap(country) {
    var provinces: LiveData<List<Province>> = Transformations.switchMap(country) {
        provinceService.getProvincesOf(it)
    }
//    var provinces = MutableLiveData<ArrayList<Province>>()

//    fun currentCountryProvinces(): LiveData<ArrayList<Province>> {
//        return provinceService.fetchProvinces(country)
//    }

//    fun fetchAllCountries(): LiveData<List<Country>> {
//        // TODO
//
//        return countries
//
//    }


    init {
        provinceService.fetchProvinces().observeForever{
            viewModelScope.launch {
                provinceService.updateLocalProvinces(it)
            }
        }

//        provinces.observeForever {
////            viewModelScope.launch{
////                provinceService.updateLocalProvinces(it)
////            }
//            provinceService.updateLocalProvinces(it)
//
//        }
        countries = provinceService.getAllCountries()
    }

}