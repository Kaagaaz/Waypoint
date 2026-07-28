package com.example.waypointv12.data

import kotlinx.coroutines.flow.Flow

class ThreatRepository(private val threatDao: ThreatDao) {
    val allThreats: Flow<List<Threat>> = threatDao.getAllThreats()

    suspend fun insert(threat: Threat) {
        threatDao.insertThreat(threat)
    }

    suspend fun clearAll() {
        threatDao.deleteAll()
    }
}
