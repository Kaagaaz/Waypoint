package com.example.waypointv12.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threats")
data class Threat(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val ipAddress: String,
    val port: Int,
    val threatType: String, // e.g., "TCP Scan", "Connection Attempt"
    val networkName: String // SSID or connection type
)
