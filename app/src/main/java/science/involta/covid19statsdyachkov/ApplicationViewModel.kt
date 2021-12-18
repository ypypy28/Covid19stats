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