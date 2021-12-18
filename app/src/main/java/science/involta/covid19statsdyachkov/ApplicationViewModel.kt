package science.involta.covid19statsdyachkov

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import science.involta.covid19statsdyachkov.data.models.Country
import science.involta.covid19statsdyachkov.data.models.City
import science.involta.covid19statsdyachkov.data.Repository

class ApplicationViewModel(app: Application): AndroidViewModel(app) {
    private var repository: Repository = Repository(app)
    val country = MutableLiveData<String>()
    var countries: LiveData<List<Country>>
    var favoriteCountries: LiveData<List<Country>>
    var cities: LiveData<List<City>> = Transformations.switchMap(country) {
        repository.getProvincesOf(it)
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

    init {
        repository.fetchCities().observeForever{
            viewModelScope.launch {
                repository.updateLocalCities(it)
            }
        }

        countries = repository.getAllCountries()
        favoriteCountries = repository.getFavoriteCountries()

    }

}