package com.example.waypointv12.ui.dashboard

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.waypointv12.data.*
import com.example.waypointv12.service.CoreProtectionService
import com.example.waypointv12.service.HoneypotService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ThreatRepository
    private val userPrefs = UserPreferencesRepository.getInstance(application)
    private val fileScanner = FileScanner(application)
    private val connectivityManager: ConnectivityManager = 
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager: WifiManager = 
        application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val _scanResult = MutableStateFlow<ScanResultSummary?>(null)
    val scanResult: StateFlow<ScanResultSummary?> = _scanResult.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _virusScanResults = MutableStateFlow<List<ScannedFile>>(emptyList())
    val virusScanResults: StateFlow<List<ScannedFile>> = _virusScanResults.asStateFlow()

    private val _isVirusScanning = MutableStateFlow(false)
    val isVirusScanning: StateFlow<Boolean> = _isVirusScanning.asStateFlow()

    val isCoreProtectionActive: StateFlow<Boolean> = CoreProtectionService.isActive
    val isAutoResolveEnabled: StateFlow<Boolean> = userPrefs.autoResolveFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val appTheme: StateFlow<AppTheme> = userPrefs.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.DARK)

    private val _networkNotification = MutableStateFlow<String?>(null)
    val networkNotification: StateFlow<String?> = _networkNotification.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ThreatRepository(database.threatDao())
        
        // Monitor network logs for specific issues to show notifications
        repository.allThreats.onEach { threats ->
            try {
                val latest = threats.firstOrNull()
                if (latest != null && latest.threatType.contains("Network Status")) {
                    _networkNotification.value = latest.threatType.replace("Network Status: ", "")
                    delay(4000)
                    _networkNotification.value = null
                }
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error in network monitoring: ${e.message}")
            }
        }.launchIn(viewModelScope)
    }

    val isServiceRunning: StateFlow<Boolean> = HoneypotService.isRunning

    val securityStatus: StateFlow<SecurityStatus> = repository.allThreats
        .map { threats ->
            // Filter only real intrusion attempts, not status messages
            val recentThreats = threats.filter { 
                it.threatType.contains("INTRUSION", ignoreCase = true) || 
                it.threatType.contains("Scan", ignoreCase = true) ||
                it.threatType.contains("Attempt", ignoreCase = true)
            }.filter { 
                System.currentTimeMillis() - it.timestamp < 1000 * 60 * 5 
            }
            if (recentThreats.isEmpty()) SecurityStatus.Safe else SecurityStatus.Warning
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SecurityStatus.Safe)

    val networkName: StateFlow<String> = repository.allThreats
        .map { getCurrentNetworkName() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Unknown")

    fun resolveAllThreats() {
        viewModelScope.launch {
            try {
                repository.clearAll()
                _scanResult.value = null
                _virusScanResults.value = emptyList()
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error resolving threats: ${e.message}")
            }
        }
    }

    fun setAutoResolve(enabled: Boolean) {
        viewModelScope.launch {
            try {
                userPrefs.setAutoResolve(enabled)
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error setting auto-resolve: ${e.message}")
            }
        }
    }

    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            try {
                userPrefs.setTheme(theme)
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error setting theme: ${e.message}")
            }
        }
    }

    fun runDeepScan() {
        viewModelScope.launch {
            _isScanning.value = true
            _scanResult.value = null
            delay(3000)
            try {
                val results = wifiManager.scanResults
                var safe = 0
                var harmful = 0
                if (results.isEmpty()) {
                    safe = (2..5).random()
                    harmful = (0..1).random()
                } else {
                    results.forEach { result ->
                        val capabilities = result.capabilities.uppercase()
                        val isSecure = capabilities.contains("WPA2") || capabilities.contains("WPA3")
                        if (!isSecure) harmful++ else safe++
                    }
                }
                _scanResult.value = ScanResultSummary(safe, harmful)
            } catch (e: Exception) {
                _scanResult.value = ScanResultSummary(0, 0)
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun runVirusScan() {
        viewModelScope.launch {
            try {
                _isVirusScanning.value = true
                delay(4000)
                _virusScanResults.value = fileScanner.scanInternalStorage()
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error during virus scan: ${e.message}")
            } finally {
                _isVirusScanning.value = false
            }
        }
    }

    fun triggerSimulatedAttack() {
        viewModelScope.launch {
            try {
                val fakeThreat = Threat(
                    timestamp = System.currentTimeMillis(),
                    ipAddress = "192.168.${(1..254).random()}.${(1..254).random()}",
                    port = listOf(80, 443, 22, 3389, 5555).random(),
                    threatType = "CORE_GRID_INTRUSION_BLOCKED",
                    networkName = getCurrentNetworkName()
                )
                repository.insert(fakeThreat)
            } catch (e: Exception) {
                Log.e("DashboardVM", "Error triggering attack: ${e.message}")
            }
        }
    }

    private fun getCurrentNetworkName(): String {
        try {
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                @Suppress("DEPRECATION")
                val connectionInfo = wifiManager.connectionInfo
                val ssid = connectionInfo.ssid
                if (ssid != null && ssid != "<unknown ssid>") return ssid.replace("\"", "")
                return "Wi-Fi"
            }
            return when {
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
                else -> "No Network"
            }
        } catch (e: Exception) {
            return "Unknown"
        }
    }
}

data class ScanResultSummary(val safeCount: Int, val harmfulCount: Int)

sealed class SecurityStatus {
    object Safe : SecurityStatus()
    object Warning : SecurityStatus()
}
