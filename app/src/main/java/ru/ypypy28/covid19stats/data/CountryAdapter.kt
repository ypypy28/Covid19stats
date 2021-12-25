package ru.ypypy28.covid19stats.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import ru.ypypy28.covid19stats.R
import ru.ypypy28.covid19stats.data.models.Country

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

        val viewFavImg: View = view.findViewById<AppCompatImageView>(R.id.fav_img)
        viewFavImg.setOnClickListener{
            onCountryClickListener.onFavClick(it as AppCompatImageView)
        }

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
        private val favorite: AppCompatImageView = itemView.findViewById(R.id.fav_img)
        private val noData: String = itemView.resources.getString(R.string.no_data)

        fun bind(country: Country) {
            countryName.text = country.name
            infected.text = country.confirmed?.toString() ?: noData
            deaths.text = country.deaths?.toString() ?: noData
            recovered.text = country.recovered?.toString() ?: noData
            if (country.isFavorite) favorite.setImageResource(R.drawable.baseline_favorite_black_24dp)
            else favorite.setImageResource(R.drawable.baseline_favorite_border_black_24dp)

            val viewFavImg: View = itemView.findViewById<AppCompatImageView>(R.id.fav_img)
            viewFavImg.tag = country
        }

    }

    interface CountryClickListener {
        fun onCountryClick(country: Country?)

        fun onFavClick(v: AppCompatImageView) {
            val country = v.tag as Country
            country.isFavorite = !country.isFavorite

            if (country.isFavorite) v.setImageResource(R.drawable.baseline_favorite_black_24dp)
            else v.setImageResource(R.drawable.baseline_favorite_border_black_24dp)
        }
    }

}