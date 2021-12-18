package science.involta.covid19statsdyachkov

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import science.involta.covid19statsdyachkov.data.Country
import science.involta.covid19statsdyachkov.data.City
import science.involta.covid19statsdyachkov.data.Repository

class ApplicationViewModel(app: Application): AndroidViewModel(app) {
    private var repository: Repository = Repository(app)
    val country = MutableLiveData<String>()
    var countries: LiveData<List<Country>>
    var favoriteCountries: LiveData<List<Country>>
    var cities: LiveData<List<City>> = Transformations.switchMap(country) {
        repository.getProvincesOf(it)
    }


    init {
        repository.fetchCities().observeForever{
            viewModelScope.launch {
                repository.updateLocalCities(it)
            }
        }

        countries = repository.getAllCountries()
        favoriteCountries = repository.getFavoriteCountries()
//        favoriteCountries.observeForever{
//            Log.d("AppViewModel", "change in favorite countries ${it.toString()}")
//        }
//
//        countries.observeForever{
//            Log.d("AppViewModel", "change in countries ${it.toString()}")
//        }

    }

    fun toggleFavoriteCountry(country: Country) {
        if (country.isFavorite) {
            viewModelScope.launch {
                repository.addCountryToFavorites(country)
            }
        } else {
            viewModelScope.launch {
                repository.removeCountryFromFavorites(country)
            }
        }
    }

}