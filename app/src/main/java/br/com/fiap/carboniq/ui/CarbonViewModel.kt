package br.com.fiap.carboniq.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.carboniq.data.AirQualityRepository
import br.com.fiap.carboniq.data.AppDatabase
import br.com.fiap.carboniq.data.CurrentAirQuality
import br.com.fiap.carboniq.data.SimulationEntry
import br.com.fiap.carboniq.data.SimulationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AirQualityState {
    object Loading : AirQualityState()
    data class Success(val data: CurrentAirQuality) : AirQualityState()
    object Error : AirQualityState()
}

class CarbonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SimulationRepository
    private val airQualityRepository = AirQualityRepository()

    private val _airQuality = MutableStateFlow<AirQualityState>(AirQualityState.Loading)
    val airQuality: StateFlow<AirQualityState> = _airQuality.asStateFlow()

    init {
        val dao = AppDatabase.getInstance(application).simulationDao()
        repository = SimulationRepository(dao)
        fetchAirQuality()
    }

    val history: StateFlow<List<SimulationEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalCo2: StateFlow<Float> = repository.totalCo2
        .map { it ?: 0f }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0f
        )

    fun saveSimulation(activityType: String, co2Value: Float) {
        viewModelScope.launch {
            repository.saveSimulation(activityType, co2Value)
        }
    }



    private fun fetchAirQuality() {
        viewModelScope.launch {
            _airQuality.value = AirQualityState.Loading
            try {
                val result = airQualityRepository.getCurrentAirQuality()
                _airQuality.value = AirQualityState.Success(result)
            } catch (e: Exception) {
                _airQuality.value = AirQualityState.Error
            }
        }
    }

    fun retryAirQuality() = fetchAirQuality()
}