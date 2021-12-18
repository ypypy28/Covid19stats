package science.involta.covid19statsdyachkov

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.data.models.Country
import science.involta.covid19statsdyachkov.data.CountryAdapter

class FragmentFavorites: Fragment(R.layout.fragment_favorites) {
    private lateinit var appViewModel: ApplicationViewModel
    private var countryAdapter: CountryAdapter? = null
    private var favoriteCountries = listOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        appViewModel = ViewModelProvider(requireActivity()).get(ApplicationViewModel::class.java)

        appViewModel.favoriteCountries.observe(viewLifecycleOwner, {
            favoriteCountries = it
            Log.d("FragmentCountries in observe", "list size is ${it.size}")
            val placeholder = view.findViewById<View>(R.id.favorite_placeholder)
            if (it.size > 0) placeholder.setVisibility(View.GONE)
            else placeholder.setVisibility(View.VISIBLE)

            setupRecyclerView(view)
        })

        setupRecyclerView(view)

        Log.d("FragmentFavoriteCountries", " list size is ${favoriteCountries.size}")

        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_favorite_countries)
        countryAdapter = CountryAdapter(favoriteCountries,
            object: CountryAdapter.CountryClickListener {
                override fun onCountryClick(country: Country?) {
                    Log.d("from FragmentFavoriteCountries", "Clicked on country ${country!!.name}")
                    appViewModel.country.value = country.name

                    activity!!.findNavController(R.id.recycler_favorite_countries)
                        .navigate(R.id.action_nav_favorites_to_nav_list_cities)
                }

                override fun onFavClick(v: AppCompatImageView) {
                    super.onFavClick(v)
                    Log.d("Favorite fragment", "onFavCLick, tag ${v.tag}")
                    appViewModel.toggleFavoriteCountry(v.tag as Country)
                }
            })

        recyclerView.adapter = countryAdapter
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

}