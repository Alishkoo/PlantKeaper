package com.example.plantchecker.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Plant
import com.example.plantchecker.R
import java.util.concurrent.TimeUnit

class PlantAdapter(
    private val onPlantClick: (Plant) -> Unit,
    private val onWateringClick: (Plant) -> Unit
) : ListAdapter<Plant, PlantAdapter.PlantViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position), onPlantClick, onWateringClick)
    }

    class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val plantName: TextView = itemView.findViewById(R.id.plant_name)
        private val plantSpecies: TextView = itemView.findViewById(R.id.plant_species)
        private val wateringStatus: TextView = itemView.findViewById(R.id.watering_status)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite_icon)
        private val plantImage: ImageView = itemView.findViewById(R.id.plant_image)

        fun bind(plant: Plant, onPlantClick: (Plant) -> Unit, onWateringClick: (Plant) -> Unit) {
            plantName.text = plant.name
            plantSpecies.text = plant.species

            // Устанавливаем иконку избранного
            favoriteIcon.visibility = if (plant.isFavorite) View.VISIBLE else View.INVISIBLE

            // Устанавливаем статус полива
            val daysUntilWatering = calculateDaysUntilWatering(plant.nextWateringDue)
            wateringStatus.text = when {
                daysUntilWatering < 0 -> "Needs water now!"
                daysUntilWatering == 0 -> "Water today"
                else -> "Water in $daysUntilWatering days"
            }

            // Обработчик клика на карточку
            itemView.setOnClickListener {
                onPlantClick(plant)
            }

            // TODO: Загрузка изображения (добавим позже)
            // Пока используем заглушку
            plantImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        private fun calculateDaysUntilWatering(nextWateringDue: Long): Int {
            val now = System.currentTimeMillis()
            val diff = nextWateringDue - now
            return TimeUnit.MILLISECONDS.toDays(diff).toInt()
        }
    }

    class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem == newItem
        }
    }
}