package com.example.plantchecker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Plant
import com.example.plantchecker.R
import com.example.plantchecker.ui.common.UiState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var plantsAdapter: PlantAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var fabAddPlant: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация View без DataBinding
        recyclerView = view.findViewById(R.id.plants_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        tabLayout = view.findViewById(R.id.tab_layout)
        fabAddPlant = view.findViewById(R.id.fab_add_plant)
        emptyView = view.findViewById(R.id.empty_view)

        setupRecyclerView()
        setupTabLayout()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        plantsAdapter = PlantAdapter(
            onPlantClick = { plant ->
                // Используем безопасную навигацию с помощью safeArgs
                val directions = HomeFragmentDirections.actionHomeToPlantDetail(plant.id)
                findNavController().navigate(directions)
            },
            onWateringClick = { plant ->
                // Обработка полива
                viewModel.waterPlant(plant.id)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = plantsAdapter
        }
    }

    private fun setupTabLayout() {
        // Сначала выберем таб All по умолчанию
        tabLayout.getTabAt(0)?.select()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setFilter(PlantFilter.All)
                    1 -> viewModel.setFilter(PlantFilter.Favorites)
                    2 -> viewModel.setFilter(PlantFilter.NeedsWatering)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupFab() {
        fabAddPlant.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeToAddPlant(null)
            findNavController().navigate(directions)
        }
    }

    private fun observeViewModel() {
        // Единый сборщик для обработки состояния UI на основе активного фильтра
        viewLifecycleOwner.lifecycleScope.launch {
            // Наблюдаем за активным фильтром и обновляем UI соответственно
            viewModel.activeFilter.collectLatest { filter ->
                // Показываем загрузку
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.GONE

                when (filter) {
                    PlantFilter.All -> {
                        // Для фильтра All используем основной список растений
                        viewModel.loadPlants()
                        viewModel.plantsState.collectLatest { state ->
                            handleUiState(state)
                        }
                    }
                    PlantFilter.Favorites -> {
                        // Для фильтра Favorites загружаем только избранные растения
                        viewModel.loadFavoritePlants()
                        viewModel.favoritePlantsState.collectLatest { state ->
                            handleUiState(state)
                        }
                    }
                    PlantFilter.NeedsWatering -> {
                        // Для фильтра NeedsWatering фильтруем по необходимости полива
                        viewModel.loadPlantsNeedingWatering()
                        viewModel.plantsNeedingWateringState.collectLatest { state ->
                            handleUiState(state)
                        }
                    }
                }
            }
        }
    }

    private fun handleUiState(state: UiState<List<Plant>>) {
        when (state) {
            is UiState.Loading -> {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.GONE
            }
            is UiState.Success -> {
                progressBar.visibility = View.GONE

                if (state.data.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE

                    // Устанавливаем текст в зависимости от фильтра
                    when (viewModel.activeFilter.value) {
                        PlantFilter.All -> emptyView.text = getString(R.string.no_plants_yet)
                        PlantFilter.Favorites -> emptyView.text = getString(R.string.no_favorites_yet)
                        PlantFilter.NeedsWatering -> emptyView.text = getString(R.string.no_plants_need_watering)
                    }
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                    plantsAdapter.submitList(state.data)
                }
            }
            is UiState.Error -> {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = state.message
            }
        }
    }
}