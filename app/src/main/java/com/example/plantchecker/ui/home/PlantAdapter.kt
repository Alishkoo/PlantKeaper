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
import com.example.plantchecker.util.ImageUtils

class PlantAdapter(
    private val onPlantClick: (Plant) -> Unit,
    private val onWaterClick: (Plant) -> Unit
) : ListAdapter<Plant, PlantAdapter.PlantViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view, onPlantClick, onWaterClick)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlantViewHolder(
        itemView: View,
        private val onPlantClick: (Plant) -> Unit,
        private val onWaterClick: (Plant) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val plantName: TextView = itemView.findViewById(R.id.plant_name)
        private val plantSpecies: TextView = itemView.findViewById(R.id.plant_species)
        private val plantImage: ImageView = itemView.findViewById(R.id.plant_image)
        // Добавляем проверку на null для кнопки полива
        private val waterButton: View? = itemView.findViewById(R.id.water_button)

        // В методе bind класса PlantViewHolder
        fun bind(plant: Plant) {
            plantName.text = plant.name
            plantSpecies.text = plant.species

            // Загружаем изображение, если оно есть
            if (plant.imageUrl.isNotEmpty()) {
                val bitmap = ImageUtils.loadImageFromPath(plant.imageUrl)
                bitmap?.let {
                    plantImage.setImageBitmap(it)
                } ?: run {
                    // Устанавливаем placeholder, если не удалось загрузить изображение
                    plantImage.setImageResource(R.drawable.ic_plant_default)
                }
            } else {
                // Устанавливаем placeholder для растений без изображения
                plantImage.setImageResource(R.drawable.ic_plant_default)
            }

            // Настраиваем слушатели
            itemView.setOnClickListener { onPlantClick(plant) }
            waterButton?.setOnClickListener { onWaterClick(plant) }
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