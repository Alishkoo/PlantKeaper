package com.example.plantchecker.ui.addplant

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.model.Plant
import com.example.plantchecker.R
import com.example.plantchecker.ui.common.UiState
import com.example.plantchecker.util.ImageUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddPlantFragment : Fragment() {

    private val args: AddPlantFragmentArgs by navArgs()
    private val viewModel: AddPlantViewModel by viewModel { parametersOf(args.plantId) }

    // Добавляем переменные для работы с изображением
    private lateinit var plantImagePreview: ImageView
    private lateinit var selectImageButton: Button
    private var currentPhotoPath: String? = null

    // Лаунчер для выбора изображения из галереи
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Сохраняем изображение и получаем путь к нему
            currentPhotoPath = ImageUtils.saveImageFromUri(requireContext(), it)
            // Отображаем выбранное изображение
            plantImagePreview.setImageURI(it)
            // Обновляем путь к изображению в ViewModel
            currentPhotoPath?.let { path ->
                viewModel.updatePlantField(PlantField.IMAGE_URL, path)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Если передан ID растения, загружаем его данные
        args.plantId?.let {
            viewModel.loadPlant(it)
        }

        // Инициализация полей
        plantImagePreview = view.findViewById(R.id.plantImagePreview)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val speciesEditText = view.findViewById<EditText>(R.id.editTextSpecies)
        val editTextSpecies = view.findViewById<EditText>(R.id.editTextSpecies)
        val editTextWateringFrequency = view.findViewById<EditText>(R.id.editTextWateringFrequency)
        val editTextSunlightNeeds = view.findViewById<EditText>(R.id.editTextSunlightNeeds)
        val editTextSoilType = view.findViewById<EditText>(R.id.editTextSoilType)
        val editTextNotes = view.findViewById<EditText>(R.id.editTextNotes)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // Если передан ID растения, загружаем его данные
        args.plantId?.let {
            if (it.isNotEmpty()) {
                viewModel.loadPlant(it)
            }
        }

        // Проверяем, пришли ли предзаполненные данные из deeplink
        arguments?.getString("species")?.let { species ->
            speciesEditText.setText(species)
            Toast.makeText(context, "Plant species pre-filled from link: $species", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем обработчик нажатия на кнопку выбора изображения
        selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Настраиваем обработчик нажатия на кнопку выбора изображения
        selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Наблюдаем за текущими данными растения
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

                // Обновляем изображение растения, если оно есть
                if (plant.imageUrl.isNotEmpty()) {
                    currentPhotoPath = plant.imageUrl
                    val bitmap = ImageUtils.loadImageFromPath(plant.imageUrl)
                    bitmap?.let {
                        plantImagePreview.setImageBitmap(it)
                    }
                }
            }
        }

        // Наблюдаем за состоянием UI
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
                        // Можно показать прогресс
                    }
                }
            }
        }

        // Сохранение изменений
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

            // Сохраняем путь к изображению, если оно было выбрано
            currentPhotoPath?.let { path ->
                viewModel.updatePlantField(PlantField.IMAGE_URL, path)
            }

            viewModel.savePlant()
        }
    }
}