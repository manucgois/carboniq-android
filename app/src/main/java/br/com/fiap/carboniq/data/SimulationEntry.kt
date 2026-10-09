package br.com.fiap.carboniq.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simulation_entries")
data class SimulationEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activityType: String,   // "Carro", "Avião", "Ônibus", "Alimentação"
    val co2Value: Float,        // kg de CO2 equivalente
    val timestamp: Long = System.currentTimeMillis()
)