package science.involta.covid19statsdyachkov.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.R

class CityAdapter(
    private val cities: List<City>,
    private val onCityClickListener: CityClickListener
    ): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityAdapter.CityViewHolder {
         val view: View = LayoutInflater.from(parent.context)
             .inflate(R.layout.province_small_prev_item, parent, false)
        view.setOnClickListener {v: View -> onCityClickListener.onCityClick(v.tag as City)}

        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityAdapter.CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
        holder.itemView.tag = city
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class CityViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        private val cityName: TextView = itemView.findViewById(R.id.city_name)
        private val infected: TextView = itemView.findViewById(R.id.city_infected_total_num)
        private val deaths: TextView = itemView.findViewById(R.id.city_deaths_total_num)
        private val recovered: TextView = itemView.findViewById(R.id.city_recovered_total_num)
        private val noData: String = ItemView.resources.getString(R.string.no_data)

        fun bind(city: City) {
            cityName.text = when (Pair(city.name != null, city.province != null)){
                Pair(true, false) -> city.name
                Pair(false, true) -> city.province
                Pair(true, true) -> "${city.name}, ${city.province}"
                else -> city.country
            }
            infected.text = city.confirmed?.toString() ?: noData
            deaths.text = city.deaths?.toString() ?: noData
            recovered.text = city.recovered?.toString() ?: noData

        }

    }

    interface CityClickListener {
        fun onCityClick(city: City?) {
            Log.d("City CLICK", "CLICK")
        }
    }
}