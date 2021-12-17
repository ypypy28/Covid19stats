package science.involta.covid19statsdyachkov

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.data.Country
import science.involta.covid19statsdyachkov.data.CountryAdapter

class FragmentFullList: Fragment(R.layout.fragment_fulllist){

    private lateinit var appViewModel: ApplicationViewModel
    private var countryAdapter: CountryAdapter? = null
    private var countries = listOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_fulllist, container, false)

        appViewModel = ViewModelProvider(requireActivity()).get(ApplicationViewModel::class.java)

        //TODO fetchCountries
//        appViewModel.fetchAllCountries().observeForever {
//            countries = it
//            Log.d("FragmentFullList in observe", "list size is ${it.size}")
//            setupRecyclerView(view)
//        }
        appViewModel.countries.observe(viewLifecycleOwner, {
            countries = it
            Log.d("FragmentFullList in observe", "list size is ${it.size}")
            setupRecyclerView(view)
        })

        setupRecyclerView(view)

        Log.d("FragmentFullList", " list size is ${countries.size}")

        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_fulllist)
        countryAdapter = CountryAdapter(countries,
            object: CountryAdapter.CountryClickListener {
                override fun onCountryClick(country: Country?) {
                    Toast.makeText(getContext(), "${country?.name}", Toast.LENGTH_SHORT).show()
                    Log.d("from FragmentFullList", "Clicked on country ${country!!.name}")
                    appViewModel.country.value = country.name
//                    activity!!.supportFragmentManager.beginTransaction()
//                        .setReorderingAllowed(true)
//                        .replace(R.id.fragment_container_view, FragmentListProvinces())
//                        .addToBackStack(null)
//                        .commit()

                    activity!!.findNavController(R.id.recycler_fulllist)
                        .navigate(R.id.action_nav_fulllist_to_nav_list_provinces2)
                }
            })

        recyclerView.adapter = countryAdapter
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

}