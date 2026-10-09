package br.com.fiap.carboniq.data

import kotlinx.coroutines.flow.Flow

class SimulationRepository(private val dao: SimulationDao) {

    val allEntries: Flow<List<SimulationEntry>> = dao.getAll()

    val totalCo2: Flow<Float?> = dao.getTotalCo2()

    suspend fun saveSimulation(activityType: String, co2Value: Float) {
        dao.insert(SimulationEntry(activityType = activityType, co2Value = co2Value))
    }
}