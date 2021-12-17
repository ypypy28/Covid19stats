package science.involta.covid19statsdyachkov.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.ApplicationViewModel
import science.involta.covid19statsdyachkov.R

class CountryAdapter(
    private val countries: List<Country>,
    private val onCountryClickListener: CountryClickListener
    ): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryAdapter.CountryViewHolder {
         val view: View = LayoutInflater.from(parent.context)
             .inflate(R.layout.country_small_prev_item, parent, false)
        view.setOnClickListener {v: View -> onCountryClickListener.onCountryClick(v.tag as Country)}

        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryAdapter.CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
        holder.itemView.tag = country
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val countryName: TextView = itemView.findViewById(R.id.country_name)
        private val infected: TextView = itemView.findViewById(R.id.country_infected_total_num)
        private val deaths: TextView = itemView.findViewById(R.id.country_deaths_total_num)
        private val recovered: TextView = itemView.findViewById(R.id.country_recovered_total_num)
        private val noData: String = itemView.resources.getString(R.string.no_data)

        fun bind(country: Country) {
            countryName.text = country.name
            infected.text = country.confirmed?.toString() ?: noData
            deaths.text = country.deaths?.toString() ?: noData
            recovered.text = country.recovered?.toString() ?: noData
        }

    }

    interface CountryClickListener {
        fun onCountryClick(country: Country?) {
            Log.d("COUNTRY CLICK", "CLICK. YOU SHOULD NEVER SEE THIS")
        }
    }
}