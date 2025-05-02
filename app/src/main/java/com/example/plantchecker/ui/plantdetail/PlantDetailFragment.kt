package com.example.plantchecker.ui.plantdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.plantchecker.R
import com.example.plantchecker.ui.common.UiState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlantDetailFragment : Fragment() {

    private val args: PlantDetailFragmentArgs by navArgs()
    private val viewModel: PlantDetailViewModel by viewModel { parametersOf(args.plantId) }

    // UI компоненты
    private lateinit var plantNameTextView: TextView
    private lateinit var plantSpeciesTextView: TextView
    private lateinit var lastWateredTextView: TextView
    private lateinit var nextWateringTextView: TextView
    private lateinit var sunlightNeedsTextView: TextView
    private lateinit var soilTypeTextView: TextView
    private lateinit var notesTextView: TextView
    private lateinit var waterButton: Button
    private lateinit var favoriteButton: ImageView
    private lateinit var plantImageView: ImageView
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем UI компоненты
        initViews(view)

        // Настраиваем слушатели для кнопок
        setupListeners()

        // Наблюдаем за данными
        observeViewModel()
    }

    private fun initViews(view: View) {
        plantNameTextView = view.findViewById(R.id.plant_name)
        plantSpeciesTextView = view.findViewById(R.id.plant_species)
        lastWateredTextView = view.findViewById(R.id.last_watered)
        nextWateringTextView = view.findViewById(R.id.next_watering)
        sunlightNeedsTextView = view.findViewById(R.id.sunlight_needs)
        soilTypeTextView = view.findViewById(R.id.soil_type)
        notesTextView = view.findViewById(R.id.notes)
        waterButton = view.findViewById(R.id.water_button)
        favoriteButton = view.findViewById(R.id.favorite_button)
        plantImageView = view.findViewById(R.id.plant_image)
        editButton = view.findViewById(R.id.edit_button)
        deleteButton = view.findViewById(R.id.delete_button)
    }

    private fun setupListeners() {
        // Кнопка полива растения
        waterButton.setOnClickListener {
            viewModel.waterPlant()
        }

        // Кнопка избранного
        favoriteButton.setOnClickListener {
            val currentFavoriteState = favoriteButton.isSelected
            viewModel.toggleFavorite(!currentFavoriteState)
        }

        // Кнопка редактирования
        editButton.setOnClickListener {
            // Исправляем навигацию - используем константу из R.id вместо сгенерированных directions
            findNavController().navigate(
                R.id.action_plantDetail_to_addPlant,
                Bundle().apply {
                    putString("plantId", args.plantId)
                }
            )
        }

        // Кнопка удаления
        deleteButton.setOnClickListener {
            // Можно добавить диалог подтверждения здесь
            viewModel.deletePlant()
        }
    }

    private fun observeViewModel() {
        // Наблюдаем за данными растения
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.plantState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        val plant = state.data
                        updateUI(plant)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {
                        // Можно показать прогресс
                    }
                }
            }
        }

        // Наблюдаем за результатами операций (полив, удаление, изменение избранного)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.operationState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        // Операция завершена успешно
                        // Для удаления - возвращаемся назад
                        if (state.data is Unit && deleteButton.visibility == View.GONE) {
                            findNavController().navigateUp()
                        }
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {
                        // Можно показать прогресс
                    }
                }
            }
        }
    }

    private fun updateUI(plant: com.example.domain.model.Plant) {
        plantNameTextView.text = plant.name
        plantSpeciesTextView.text = plant.species

        // Форматирование дат
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        lastWateredTextView.text = dateFormat.format(Date(plant.lastWateredTimestamp))
        nextWateringTextView.text = dateFormat.format(Date(plant.nextWateringDue))

        // Дополнительные данные
        sunlightNeedsTextView.text = plant.sunlightNeeds.takeIf { it.isNotEmpty() } ?: "Not specified"
        soilTypeTextView.text = plant.soilType.takeIf { it.isNotEmpty() } ?: "Not specified"
        notesTextView.text = plant.notes.takeIf { it.isNotEmpty() } ?: "No notes"

        // Обновляем состояние избранного
        favoriteButton.isSelected = plant.isFavorite
        favoriteButton.setImageResource(
            if (plant.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )
    }
}