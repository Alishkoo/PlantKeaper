package com.example.plantchecker.ui.addplant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.plantchecker.R
import com.example.plantchecker.ui.common.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddPlantFragment : Fragment() {

    private val args: AddPlantFragmentArgs by navArgs()
    private val viewModel: AddPlantViewModel by viewModel { parametersOf(args.plantId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.plantId?.let {
            viewModel.loadPlant(it)
        }

        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextSpecies = view.findViewById<EditText>(R.id.editTextSpecies)
        val editTextWateringFrequency = view.findViewById<EditText>(R.id.editTextWateringFrequency)
        val editTextSunlightNeeds = view.findViewById<EditText>(R.id.editTextSunlightNeeds)
        val editTextSoilType = view.findViewById<EditText>(R.id.editTextSoilType)
        val editTextNotes = view.findViewById<EditText>(R.id.editTextNotes)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.plant.collectLatest { plant ->
                editTextName.setText(plant.name)
                editTextSpecies.setText(plant.species)
                editTextWateringFrequency.setText(
                    if (plant.wateringFrequencyDays > 0) plant.wateringFrequencyDays.toString() else ""
                )
                editTextSunlightNeeds.setText(plant.sunlightNeeds)
                editTextSoilType.setText(plant.soilType)
                editTextNotes.setText(plant.notes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        if (state.data.isNotEmpty()) {
                            Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
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

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val species = editTextSpecies.text.toString().trim()
            val wateringFrequencyText = editTextWateringFrequency.text.toString().trim()
            val sunlightNeeds = editTextSunlightNeeds.text.toString().trim()
            val soilType = editTextSoilType.text.toString().trim()
            val notes = editTextNotes.text.toString().trim()

            viewModel.updatePlantField(PlantField.NAME, name)
            viewModel.updatePlantField(PlantField.SPECIES, species)

            if (wateringFrequencyText.isNotEmpty()) {
                try {
                    viewModel.updatePlantField(PlantField.WATERING_FREQUENCY, wateringFrequencyText.toInt())
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Please enter a valid number for watering frequency", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            viewModel.updatePlantField(PlantField.SUNLIGHT_NEEDS, sunlightNeeds)
            viewModel.updatePlantField(PlantField.SOIL_TYPE, soilType)
            viewModel.updatePlantField(PlantField.NOTES, notes)

            viewModel.savePlant()
        }
    }
}