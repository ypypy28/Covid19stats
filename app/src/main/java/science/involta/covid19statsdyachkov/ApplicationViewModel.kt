package science.involta.covid19statsdyachkov

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import science.involta.covid19statsdyachkov.data.Country
import science.involta.covid19statsdyachkov.data.City
import science.involta.covid19statsdyachkov.data.CityRepository

class ApplicationViewModel(app: Application): AndroidViewModel(app) {
    private var cityRepository: CityRepository = CityRepository(app)
    val country = MutableLiveData<String>()
    var countries: LiveData<List<Country>>
    var cities: LiveData<List<City>> = Transformations.switchMap(country) {
        cityRepository.getProvincesOf(it)
    }


    init {
        cityRepository.fetchCities().observeForever{
            viewModelScope.launch {
                cityRepository.updateLocalCities(it)
            }
        }

        countries = cityRepository.getAllCountries()
    }

}