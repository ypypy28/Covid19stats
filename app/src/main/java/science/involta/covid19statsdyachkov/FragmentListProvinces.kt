package science.involta.covid19statsdyachkov

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.data.Province
import science.involta.covid19statsdyachkov.data.ProvinceAdapter

class FragmentListProvinces: Fragment(R.layout.fragment_list_provinces) {
    private lateinit var appViewModel: ApplicationViewModel
    private var provinceAdapter: ProvinceAdapter? = null
    private var provinces: List<Province> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list_provinces, container, false)

        appViewModel = ViewModelProvider(requireActivity()).get(ApplicationViewModel::class.java)

//        appViewModel.currentCountryProvinces().observeForever {
//            provinces = it
//            Log.d("FragmentListProvinces in observe", "size is ${it.size}")
//            setupRecyclerView(view)
//        }

//        appViewModel.currentCountryProvinces().observe(
        appViewModel.provinces.observe(
            viewLifecycleOwner,
            Observer {
                    provinces = it
                    Log.d("FragmentListProvinces in observe", "size is ${it.size}")
                    setupRecyclerView(view)
        })

        Log.d("FragmentListProvinces", "size is ${provinces.size}")
//        provinces = mutableListOf(
//            Province(name="Texas", country="United States", keyId='Texas, United States', confirmed=2000, deaths=1000, recovered=800),
//            Province(name="Ivanovkaya", country="Russia", keyId='Ivanovskaya, Russia', confirmed=4037, deaths=2101, recovered=1706),
//            Province(name="Vladimirskaya", country="Russia", keyId='Vladimirskaya, Russia', confirmed=4037, deaths=2101, recovered=1706),
//            Province(name="Tennesy", country="Uniteed States", keyId='Tennesy, United States', confirmed=4037, deaths=2101, recovered=1706),
//            Province(name="Yaroslavskaya", country="Russia", keyId='Yaroslavskaya, Russia', confirmed=14037, deaths=5110, recovered=7048),
//            Province(name="Moscow", country="Russia", keyId='Moscow, Russia', confirmed=897037, deaths=121013, recovered=65006),
//            Province(name="Moskovskaya", country="Russia", keyId='Moskovskaya, Russia', confirmed=4037, deaths=2101, recovered=1706),
//        )

        Log.d("FragmentListProvinces", "onCreateView ProvincesList" )

        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_provinces)
        provinceAdapter = ProvinceAdapter(provinces,
            object:ProvinceAdapter.ProvinceClickListener {
            override fun onProvinceClick(province: Province?) {
                Toast.makeText(getContext(), "${province?.lastUpdate}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.adapter = provinceAdapter
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

}