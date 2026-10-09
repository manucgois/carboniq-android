package br.com.fiap.carboniq.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulationDao {

    @Insert
    suspend fun insert(entry: SimulationEntry)

    @Query("SELECT * FROM simulation_entries ORDER BY timestamp DESC")
    fun getAll(): Flow<List<SimulationEntry>>

    @Query("SELECT SUM(co2Value) FROM simulation_entries")
    fun getTotalCo2(): Flow<Float?>
}