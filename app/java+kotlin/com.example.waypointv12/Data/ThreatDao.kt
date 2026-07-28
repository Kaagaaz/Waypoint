package com.example.waypointv12.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreatDao {
    @Query("SELECT * FROM threats ORDER BY timestamp DESC")
    fun getAllThreats(): Flow<List<Threat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThreat(threat: Threat)

    @Query("DELETE FROM threats")
    suspend fun deleteAll()
}
