package ru.ypypy28.covid19stats

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.ypypy28.covid19stats.data.models.Country
import ru.ypypy28.covid19stats.data.models.City
import ru.ypypy28.covid19stats.data.Repository

class ApplicationViewModel(app: Application): AndroidViewModel(app) {
    private var repository: Repository = Repository(app)
    val country = MutableLiveData<String>()
    var countries: LiveData<List<Country>> = repository.getAllCountries()
    var favoriteCountries: LiveData<List<Country>> = repository.getFavoriteCountries()
    var cities: LiveData<List<City>> = Transformations.switchMap(country) {
        repository.getCitiesOf(it)
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
        repository.fetchData(viewModelScope, null)

    }

}