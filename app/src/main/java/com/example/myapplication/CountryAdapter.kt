package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountryAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Находим View по ID при создании ViewHolder
        val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        val tvCapital: TextView = itemView.findViewById(R.id.tvCapital)
        val tvRegion: TextView = itemView.findViewById(R.id.tvRegion)
        val tvPopulation: TextView = itemView.findViewById(R.id.tvPopulation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.tvCountryName.text = country.name.common
        holder.tvCapital.text = "Столица: ${country.capital?.firstOrNull() ?: "Нет"}"
        holder.tvRegion.text = "Регион: ${country.region}"
        holder.tvPopulation.text = "Население: ${country.population}"
    }

    override fun getItemCount() = countries.size
}