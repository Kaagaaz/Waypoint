package com.example.waypointv12.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.waypointv12.ui.dashboard.DashboardScreen
import com.example.waypointv12.ui.log.LogScreen
import com.example.waypointv12.ui.menu.MenuScreen
import com.example.waypointv12.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun WayPointAdaptiveScaffold(
    onStartService: () -> Unit,
    onStopService: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Destination>()
    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ListDetailPaneScaffold(
            modifier = Modifier.padding(innerPadding),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                DashboardScreen(
                    onNavigateToMenu = {
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, Destination.Menu)
                        }
                    },
                    onStartService = onStartService,
                    onStopService = onStopService
                )
            },
            detailPane = {
                val currentDest = navigator.currentDestination?.contentKey
                when (currentDest) {
                    Destination.Menu -> {
                        MenuScreen(
                            onNavigateBack = {
                                scope.launch { navigator.navigateBack() }
                            },
                            onNavigateToLog = {
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, Destination.ThreatLog)
                                }
                            },
                            onNavigateToSettings = {
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, Destination.Settings)
                                }
                            }
                        )
                    }
                    Destination.ThreatLog -> {
                        LogScreen(
                            onNavigateBack = {
                                scope.launch { navigator.navigateBack() }
                            }
                        )
                    }
                    Destination.Settings -> {
                        SettingsScreen(
                            onNavigateBack = {
                                scope.launch { navigator.navigateBack() }
                            }
                        )
                    }
                    else -> {
                        // Default content if no detail pane is selected on large screens
                    }
                }
            }
        )
    }
}
