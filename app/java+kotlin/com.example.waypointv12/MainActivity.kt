package com.example.waypointv12

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import android.net.VpnService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.data.AppTheme
import com.example.waypointv12.service.CoreProtectionService
import com.example.waypointv12.service.HoneypotService
import com.example.waypointv12.ui.dashboard.DashboardViewModel
import com.example.waypointv12.ui.navigation.WayPointAdaptiveScaffold
import com.example.waypointv12.ui.theme.WayPointv12Theme
import com.google.accompanist.permissions.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dashboardViewModel: DashboardViewModel = viewModel()
            val themeState by dashboardViewModel.appTheme.collectAsState()
            
            val isDarkTheme = when (themeState) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            WayPointv12Theme(darkTheme = isDarkTheme, dynamicColor = false) {
                MainContainer(
                    onStartService = { startHoneypotService() },
                    onStopService = { stopHoneypotService() }
                )
            }
        }
    }

    private fun startHoneypotService() {
        val vpnIntent = VpnService.prepare(this)
        if (vpnIntent != null) {
            startActivity(vpnIntent)
        } else {
            startService(Intent(this, CoreProtectionService::class.java))
        }

        val intent = Intent(this, HoneypotService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopHoneypotService() {
        stopService(Intent(this, CoreProtectionService::class.java))
        val intent = Intent(this, HoneypotService::class.java)
        stopService(intent)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContainer(onStartService: () -> Unit, onStopService: () -> Unit) {
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val permissionState = rememberMultiplePermissionsState(permissions)

    if (permissionState.allPermissionsGranted) {
        WayPointAdaptiveScaffold(
            onStartService = onStartService,
            onStopService = onStopService
        )
    } else {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.permissions_required))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    Text(stringResource(R.string.grant_permissions))
                }
            }
        }
    }
}
