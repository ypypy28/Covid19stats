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
import science.involta.covid19statsdyachkov.data.Country
import science.involta.covid19statsdyachkov.data.CountryAdapter

class FragmentCountries: Fragment(R.layout.fragment_countries){

    private lateinit var appViewModel: ApplicationViewModel
    private var countryAdapter: CountryAdapter? = null
    private var countries = listOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_countries, container, false)

        appViewModel = ViewModelProvider(requireActivity()).get(ApplicationViewModel::class.java)

        appViewModel.countries.observe(viewLifecycleOwner, {
            countries = it
            Log.d("FragmentCountries in observe", "list size is ${it.size}")
            setupRecyclerView(view)
        })

        setupRecyclerView(view)

        Log.d("FragmentCountries", " list size is ${countries.size}")

        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_countries)
        countryAdapter = CountryAdapter(countries,
            object : CountryAdapter.CountryClickListener {
                override fun onCountryClick(country: Country?) {
//                    Toast.makeText(getContext(), "${country?.name}", Toast.LENGTH_SHORT).show()
                    Log.d("from FragmentCountries", "Clicked on country ${country!!.name}")
                    appViewModel.country.value = country.name
//                    activity!!.supportFragmentManager.beginTransaction()
//                        .setReorderingAllowed(true)
//                        .replace(R.id.fragment_container_view, FragmentListCities())
//                        .addToBackStack(null)
//                        .commit()

                    activity!!.findNavController(R.id.recycler_countries)
                        .navigate(R.id.action_nav_countries_to_nav_list_cities)
                }

                override fun onFavClick(v: AppCompatImageView) {
                    super.onFavClick(v)
                    Log.d("FragmentCountry", "OnFavCLick, tag ${v.tag}")
                    appViewModel.toggleFavoriteCountry(v.tag as Country)
                }
            })

        recyclerView.adapter = countryAdapter
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

}