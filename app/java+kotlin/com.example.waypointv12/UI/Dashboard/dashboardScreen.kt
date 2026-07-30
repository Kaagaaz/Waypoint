package com.example.waypointv12.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.GppGood
import androidx.compose.material.icons.rounded.GppMaybe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.R
import com.example.waypointv12.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToMenu: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val status by viewModel.securityStatus.collectAsState()
    val network by viewModel.networkName.collectAsState()
    val isRunning by viewModel.isServiceRunning.collectAsState()
    val isVirusScanning by viewModel.isVirusScanning.collectAsState()
    val virusResults by viewModel.virusScanResults.collectAsState()
    val isCoreActive by viewModel.isCoreProtectionActive.collectAsState()
    val isAutoResolveEnabled by viewModel.isAutoResolveEnabled.collectAsState()
    val networkNotify by viewModel.networkNotification.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            stringResource(R.string.dashboard_title), 
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        ) 
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = onNavigateToMenu) {
                            Icon(
                                Icons.Default.MoreVert, 
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
                
                // Top Notification for Network Changes
                AnimatedVisibility(
                    visible = networkNotify != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NETWORK LINK: ${networkNotify?.uppercase()}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeuralThreatMatrix(status)

            // Core Status Panels
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TechPanel(
                    Modifier.weight(1f), 
                    "CORE LINK", 
                    if (isCoreActive) "SHIELD ACTIVE" else if (isRunning) "SCANNING" else "OFFLINE", 
                    if (isCoreActive) MaterialTheme.colorScheme.primary else if (isRunning) MaterialTheme.colorScheme.tertiary else Color.Gray
                )
                TechPanel(
                    Modifier.weight(1f), 
                    "FIREWALL", 
                    if (isCoreActive) "LOCKED" else if (isRunning) "ARMED" else "OFFLINE", 
                    if (isCoreActive) MaterialTheme.colorScheme.secondary else if (isRunning) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                )
            }

            // Current Network Information
            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(stringResource(R.string.current_network), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                    Text(network, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }

            // AI AUTO-RESOLVE & SUB-OPTION
            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Main Action Button
                    HyperButton(
                        text = if (isAutoResolveEnabled) "AI MONITORING ACTIVE" else stringResource(R.string.resolve_all_threats),
                        onClick = { viewModel.resolveAllThreats() },
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isAutoResolveEnabled && status is SecurityStatus.Warning
                    )

                    // Sub-Option (Indented and scaled down)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "AUTO-RESOLVE ENGINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                        )
                        Switch(
                            checked = isAutoResolveEnabled,
                            onCheckedChange = { viewModel.setAutoResolve(it) },
                            modifier = Modifier.scale(0.7f),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }

            // NEURAL VIRUS SCAN
            HyperButton(
                text = "NEURAL VIRUS SCAN",
                onClick = { viewModel.runVirusScan() },
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isVirusScanning
            )

            if (isVirusScanning) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary)
            } else if (virusResults.isNotEmpty()) {
                TechPanel(Modifier.fillMaxWidth(), "SCAN COMPLETED", "${virusResults.size} OBJECTS ANALYZED", MaterialTheme.colorScheme.tertiary)
            }

            // PROTECTION TOGGLES
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (!isRunning) {
                    HyperButton(stringResource(R.string.enable_protection), onStartService, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                } else {
                    HyperButton(stringResource(R.string.disable_protection), onStopService, MaterialTheme.colorScheme.error, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TechPanel(modifier: Modifier = Modifier, label: String, status: String, color: Color) {
    Box(
        modifier = modifier
            .height(70.dp)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.weight(1f))
            Text(status, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun NeuralThreatMatrix(status: SecurityStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarRotation"
    )

    val color = if (status is SecurityStatus.Safe) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
    val text = if (status is SecurityStatus.Safe) "SYSTEM SECURE" else "INTRUSION BLOCKED"
    val integrity = if (status is SecurityStatus.Safe) "100% INTEGRITY" else "GRID COMPROMISED"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .border(2.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(color.copy(alpha = 0.1f), Color.Transparent)), RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(color, radius = size.width / 2, style = Stroke(width = 2f), alpha = 0.2f)
                drawCircle(color, radius = size.width / 3, style = Stroke(width = 2f), alpha = 0.2f)
                drawLine(color, start = Offset(0f, center.y), end = Offset(size.width, center.y), alpha = 0.2f, strokeWidth = 2f)
                drawLine(color, start = Offset(center.x, 0f), end = Offset(center.x, size.height), alpha = 0.2f, strokeWidth = 2f)
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    brush = Brush.sweepGradient(0f to Color.Transparent, 0.5f to color.copy(alpha = 0.4f), 1f to Color.Transparent, center = Offset(size.width / 2, size.height / 2)),
                    startAngle = rotation,
                    sweepAngle = 90f,
                    useCenter = true
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(if (status is SecurityStatus.Safe) Icons.Rounded.GppGood else Icons.Rounded.GppMaybe, null, Modifier.size(40.dp), color)
                Text(text, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
        }
        Text(integrity, color = color, style = MaterialTheme.typography.bodySmall, letterSpacing = 2.sp)
    }
}

@Composable
fun HyperButton(text: String, onClick: () -> Unit, color: Color, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(48.dp)
            .border(1.dp, if (enabled) color else color.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color,
            disabledContainerColor = color.copy(alpha = 0.05f),
            disabledContentColor = color.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 12.sp)
    }
}
