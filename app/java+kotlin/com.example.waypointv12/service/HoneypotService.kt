package com.example.waypointv12.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.waypointv12.R
import com.example.waypointv12.data.AppDatabase
import com.example.waypointv12.data.Threat
import com.example.waypointv12.data.ThreatRepository
import com.example.waypointv12.data.UserPreferencesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.net.ServerSocket
import java.net.SocketException

class HoneypotService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var repository: ThreatRepository? = null
    private var userPrefs: UserPreferencesRepository? = null
    private var connectivityManager: ConnectivityManager? = null
    
    private val ports = listOf(8080, 8443, 2222, 5555, 3389)
    private val serverSockets = mutableListOf<ServerSocket>()

    companion object {
        private const val TAG = "HoneypotService"
        private const val CHANNEL_ID = "honeypot_channel"
        private const val NOTIFICATION_ID = 1

        private val _isRunning = MutableStateFlow(false)
        val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            logNetworkChange("Network Available")
        }

        override fun onLost(network: Network) {
            logNetworkChange("Network Lost")
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            val isWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            Log.d(TAG, "Network capabilities changed: WiFi=$isWifi")
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            val database = AppDatabase.getDatabase(this)
            repository = ThreatRepository(database.threatDao())
            userPrefs = UserPreferencesRepository.getInstance(this)
            connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, createNotification(getString(R.string.monitoring_network)))
            
            registerNetworkCallback()
            startHoneypot()
        } catch (e: Exception) {
            Log.e(TAG, "Critical error in onCreate: ${e.message}")
            stopSelf()
        }
    }

    private fun registerNetworkCallback() {
        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Error registering network callback: ${e.message}")
        }
    }

    private fun startHoneypot() {
        ports.forEach { port ->
            serviceScope.launch {
                try {
                    val serverSocket = ServerSocket(port)
                    synchronized(serverSockets) {
                        serverSockets.add(serverSocket)
                    }
                    Log.d(TAG, "Listening on port $port")
                    while (isActive) {
                        val clientSocket = serverSocket.accept()
                        val remoteAddress = clientSocket.inetAddress.hostAddress
                        Log.w(TAG, "Connection attempt from $remoteAddress on port $port")
                        
                        saveThreat(remoteAddress ?: "Unknown", port, "Port Scan/Connection")
                        
                        clientSocket.close()
                    }
                } catch (e: SocketException) {
                    Log.e(TAG, "Socket error on port $port: ${e.message}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error on port $port: ${e.message}")
                }
            }
        }
    }

    private fun saveThreat(ip: String, port: Int, type: String) {
        serviceScope.launch {
            try {
                val threat = Threat(
                    timestamp = System.currentTimeMillis(),
                    ipAddress = ip,
                    port = port,
                    threatType = type,
                    networkName = getCurrentNetworkName()
                )
                repository?.insert(threat)
                updateNotification("Threat detected from $ip")

                // Engage Core Protection immediately on attack
                engageAutoShield()

                // Auto-Resolve Logic with safety
                val prefs = userPrefs
                if (prefs != null) {
                    val isAutoResolveEnabled = prefs.autoResolveFlow.firstOrNull() ?: false
                    if (isAutoResolveEnabled) {
                        delay(2000)
                        repository?.clearAll()
                        Log.i(TAG, "AI Auto-Resolve: Security grid restored.")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in saveThreat: ${e.message}")
            }
        }
    }

    private fun engageAutoShield() {
        try {
            val intent = Intent(this, CoreProtectionService::class.java)
            startService(intent)
            Log.i(TAG, "Dynamic Auto-Shield Engaged: Firewall Active")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to engage Auto-Shield: ${e.message}")
        }
    }

    private fun logNetworkChange(status: String) {
        serviceScope.launch {
            try {
                val threat = Threat(
                    timestamp = System.currentTimeMillis(),
                    ipAddress = "N/A",
                    port = 0,
                    threatType = "Network Status: $status",
                    networkName = getCurrentNetworkName()
                )
                repository?.insert(threat)
            } catch (e: Exception) {
                Log.e(TAG, "Error in logNetworkChange: ${e.message}")
            }
        }
    }

    private fun getCurrentNetworkName(): String {
        try {
            val cm = connectivityManager ?: return "Unknown"
            val activeNetwork = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(activeNetwork)
            
            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                @Suppress("DEPRECATION")
                val connectionInfo = wifiManager.connectionInfo
                val ssid = connectionInfo.ssid
                if (ssid != null && ssid != "<unknown ssid>") {
                    return ssid.replace("\"", "")
                }
                return "Wi-Fi"
            }
            
            return when {
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
                else -> "Unknown"
            }
        } catch (e: Exception) {
            return "Unknown"
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.honeypot_service_channel),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WayPoint Security")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(content: String) {
        try {
            val notification = createNotification(content)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating notification: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _isRunning.value = true
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        try {
            _isRunning.value = false
            serviceScope.cancel()
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            synchronized(serverSockets) {
                serverSockets.forEach { 
                    try { it.close() } catch (e: Exception) { }
                }
                serverSockets.clear()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onDestroy: ${e.message}")
        }
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }
}
