package ru.ypypy28.covid19stats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ypypy28.covid19stats.data.models.City
import ru.ypypy28.covid19stats.data.CityAdapter
import java.text.SimpleDateFormat

class FragmentListCities: Fragment(R.layout.fragment_list_cities) {
    private lateinit var appViewModel: ApplicationViewModel
    private var cityAdapter: CityAdapter? = null
    private lateinit var curCountry: TextView
    private var cities: List<City> = listOf()

    private val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    private val dateFormatter = SimpleDateFormat("HH:mm dd MMM yyy ")
    private lateinit var dataUpdateString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list_cities, container, false)

        dataUpdateString = resources.getString(R.string.lastUpdateString)

        appViewModel = ViewModelProvider(requireActivity()).get(ApplicationViewModel::class.java)

        curCountry = view.findViewById(R.id.cities_cur_country)
        appViewModel.country.observe(
            viewLifecycleOwner,
            {
                curCountry.text = it
            }
        )

        appViewModel.cities.observe(
            viewLifecycleOwner,
            {
                cities = it
                Log.d("FragmentListCities in observe", "size is ${it.size}")
                setupRecyclerView(view)
            })

        Log.d("FragmentListCities", "size is ${cities.size}")
//        cities = mutableListOf(
//            City(name="Texas", country="United States", keyId="Texas, United States", confirmed=2000, deaths=1000, recovered=800),
//            City(name="Ivanovkaya", country="Russia", keyId="Ivanovskaya, Russia", confirmed=4037, deaths=2101, recovered=1706),
//            City(name="Vladimirskaya", country="Russia", keyId="Vladimirskaya, Russia", confirmed=4037, deaths=2101, recovered=1706),
//            City(name="Tennesy", country="Uniteed States", keyId="Tennesy, United States", confirmed=4037, deaths=2101, recovered=1706),
//            City(name="Yaroslavskaya", country="Russia", keyId="Yaroslavskaya, Russia", confirmed=14037, deaths=5110, recovered=7048),
//            City(name="Moscow", country="Russia", keyId="Moscow, Russia", confirmed=897037, deaths=121013, recovered=65006),
//            City(name="Moskovskaya", country="Russia", keyId="Moskovskaya, Russia", confirmed=4037, deaths=2101, recovered=1706),
//        )

        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_cities)
        cityAdapter = CityAdapter(cities,
            object:CityAdapter.CityClickListener {
            override fun onCityClick(city: City?) {
                Log.d("COUNTRYCLICK", "${city?.lastUpdate}")
                try {
                    val updated = dateFormatter.format(dateParser.parse(city!!.lastUpdate))
                    Toast.makeText(getContext(), "${dataUpdateString} ${updated}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.d("COUNTRYCLICK Exception", e.message.toString())
                }
            }
        })

        recyclerView.adapter = cityAdapter
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

}