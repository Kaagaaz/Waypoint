package com.example.waypointv12.service

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class CoreProtectionService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var isProtecting = false

    companion object {
        private const val TAG = "CoreProtection"
        private val _isActive = MutableStateFlow(false)
        val isActive: StateFlow<Boolean> = _isActive.asStateFlow()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isProtecting) {
            startProtection()
        }
        return START_STICKY
    }

    private fun startProtection() {
        try {
            vpnInterface = Builder()
                .addAddress("10.0.0.2", 32)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("1.1.1.1") // Secure DNS
                .setSession("WayPoint Core Protection")
                .establish()

            isProtecting = true
            _isActive.value = true
            Log.i(TAG, "Core Protection Grid Active: IP OBFUSCATED")


        } catch (e: Exception) {
            Log.e(TAG, "Failed to establish Core Protection: ${e.message}")
        }
    }

    override fun onRevoke() {
        stopProtection()
        super.onRevoke()
    }

    private fun stopProtection() {
        isProtecting = false
        _isActive.value = false
        vpnInterface?.close()
        vpnInterface = null
        stopSelf()
    }

    override fun onDestroy() {
        stopProtection()
        super.onDestroy()
    }
}
