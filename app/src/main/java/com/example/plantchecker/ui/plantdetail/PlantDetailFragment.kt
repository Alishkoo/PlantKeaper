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
import com.example.domain.model.Plant
import com.example.plantchecker.R
import com.example.plantchecker.ui.common.UiState
import com.example.plantchecker.util.ImageUtils
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


        viewModel.loadPlant(args.plantId)


        if (activity?.intent?.data?.toString()?.startsWith("plantchecker://plant/") == true) {
            Toast.makeText(context, "Opened from deep link: ${activity?.intent?.data}", Toast.LENGTH_SHORT).show()
        }


        initViews(view)


        setupListeners()


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
        waterButton.setOnClickListener {
            viewModel.waterPlant()
        }

        favoriteButton.setOnClickListener {
            val currentFavoriteState = favoriteButton.isSelected
            viewModel.toggleFavorite(!currentFavoriteState)
        }


        editButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_plantDetail_to_addPlant,
                Bundle().apply {
                    putString("plantId", args.plantId)
                }
            )
        }


        deleteButton.setOnClickListener {
            viewModel.deletePlant()
        }
    }

    private fun observeViewModel() {

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

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.operationState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        if (state.data is Unit && deleteButton.visibility == View.GONE) {
                            findNavController().navigateUp()
                        }
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {

                    }
                }
            }
        }
    }

    private fun updateUI(plant: Plant) {
        plantNameTextView.text = plant.name
        plantSpeciesTextView.text = plant.species


        if (plant.imageUrl.isNotEmpty()) {
            val bitmap = ImageUtils.loadImageFromPath(plant.imageUrl)
            bitmap?.let {
                plantImageView.setImageBitmap(it)
            } ?: run {

                plantImageView.setImageResource(R.drawable.ic_plant_default)
            }
        } else {
            plantImageView.setImageResource(R.drawable.ic_plant_default)
        }


        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        lastWateredTextView.text = dateFormat.format(Date(plant.lastWateredTimestamp))
        nextWateringTextView.text = dateFormat.format(Date(plant.nextWateringDue))


        sunlightNeedsTextView.text = plant.sunlightNeeds.takeIf { it.isNotEmpty() } ?: "Not specified"
        soilTypeTextView.text = plant.soilType.takeIf { it.isNotEmpty() } ?: "Not specified"
        notesTextView.text = plant.notes.takeIf { it.isNotEmpty() } ?: "No notes"


        favoriteButton.isSelected = plant.isFavorite
        favoriteButton.setImageResource(
            if (plant.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )
    }
}