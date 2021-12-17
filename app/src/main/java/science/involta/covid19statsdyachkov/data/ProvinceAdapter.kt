package science.involta.covid19statsdyachkov.data

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import science.involta.covid19statsdyachkov.R

class ProvinceAdapter(
    private val provinces: List<Province>,
    private val onProvinceClickListener: ProvinceClickListener
    ): RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProvinceAdapter.ProvinceViewHolder {
         val view: View = LayoutInflater.from(parent.context)
             .inflate(R.layout.province_small_prev_item, parent, false)
        view.setOnClickListener {v: View -> onProvinceClickListener.onProvinceClick(v.tag as Province)}

        return ProvinceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProvinceAdapter.ProvinceViewHolder, position: Int) {
        val province = provinces[position]
        holder.bind(province)
        holder.itemView.tag = province
    }

    override fun getItemCount(): Int {
        return provinces.size
    }

    class ProvinceViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        private val provinceName: TextView = itemView.findViewById(R.id.province_name)
        private val infected: TextView = itemView.findViewById(R.id.province_infected_total_num)
        private val deaths: TextView = itemView.findViewById(R.id.province_deaths_total_num)
        private val recovered: TextView = itemView.findViewById(R.id.province_recovered_total_num)
        private val noData: String = ItemView.resources.getString(R.string.no_data)

        fun bind(province: Province) {
//            provinceName.text = province.name ?: province.country
            provinceName.text = province.keyId
            infected.text = province.confirmed?.toString() ?: noData
            deaths.text = province.deaths?.toString() ?: noData
            recovered.text = province.recovered?.toString() ?: noData

        }

    }

    interface ProvinceClickListener {
        fun onProvinceClick(province: Province?) {
            Log.d("PROVINCE CLICK", "CLICK")
        }
    }
}