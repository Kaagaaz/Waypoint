package com.example.waypointv12.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.R
import com.example.waypointv12.data.Threat
import com.example.waypointv12.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    onNavigateBack: () -> Unit,
    viewModel: LogViewModel = viewModel()
) {
    val threats by viewModel.threats.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new threats arrive
    LaunchedEffect(threats.size) {
        if (threats.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.log_title), 
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back), tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearLogs() }) {
                        Icon(Icons.Rounded.DeleteSweep, contentDescription = stringResource(R.string.clear_logs), tint = NeonRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Terminal Header
            TerminalHeader()
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(threats) { threat ->
                    TerminalThreatEntry(threat)
                }
            }
        }
    }
}

@Composable
fun TerminalHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(NeonRed, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonYellow, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonGreen, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text("SEC_TERMINAL_V1.2", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TerminalThreatEntry(threat: Threat) {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(threat.timestamp))

    // We keep the functional colors (Yellow/Red) even in Light mode as they indicate status,
    // but we use slightly deeper shades in the theme definition already.
    val (tag, color) = when {
        threat.threatType.contains("Scan", ignoreCase = true) -> "[WARN]" to MaterialTheme.colorScheme.tertiary
        threat.threatType.contains("Intrusion", ignoreCase = true) -> "[ERR!]" to MaterialTheme.colorScheme.error
        threat.threatType.contains("Status", ignoreCase = true) -> "[INFO]" to MaterialTheme.colorScheme.primary
        else -> "[OK  ]" to MaterialTheme.colorScheme.secondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row {
            Text(
                text = "$dateString ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "$tag ",
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = threat.threatType,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = " > SRC: ${threat.ipAddress} | PORT: ${threat.port} | NET: ${threat.networkName}",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), thickness = 0.5.dp)
    }
}package com.example.waypointv12.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.R
import com.example.waypointv12.data.Threat
import com.example.waypointv12.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    onNavigateBack: () -> Unit,
    viewModel: LogViewModel = viewModel()
) {
    val threats by viewModel.threats.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new threats arrive
    LaunchedEffect(threats.size) {
        if (threats.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.log_title), 
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back), tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearLogs() }) {
                        Icon(Icons.Rounded.DeleteSweep, contentDescription = stringResource(R.string.clear_logs), tint = NeonRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Terminal Header
            TerminalHeader()
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(threats) { threat ->
                    TerminalThreatEntry(threat)
                }
            }
        }
    }
}

@Composable
fun TerminalHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(NeonRed, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonYellow, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonGreen, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text("SEC_TERMINAL_V1.2", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TerminalThreatEntry(threat: Threat) {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(threat.timestamp))

    // We keep the functional colors (Yellow/Red) even in Light mode as they indicate status,
    // but we use slightly deeper shades in the theme definition already.
    val (tag, color) = when {
        threat.threatType.contains("Scan", ignoreCase = true) -> "[WARN]" to MaterialTheme.colorScheme.tertiary
        threat.threatType.contains("Intrusion", ignoreCase = true) -> "[ERR!]" to MaterialTheme.colorScheme.error
        threat.threatType.contains("Status", ignoreCase = true) -> "[INFO]" to MaterialTheme.colorScheme.primary
        else -> "[OK  ]" to MaterialTheme.colorScheme.secondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row {
            Text(
                text = "$dateString ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "$tag ",
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = threat.threatType,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = " > SRC: ${threat.ipAddress} | PORT: ${threat.port} | NET: ${threat.networkName}",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), thickness = 0.5.dp)
    }
}package com.example.waypointv12.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.R
import com.example.waypointv12.data.Threat
import com.example.waypointv12.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    onNavigateBack: () -> Unit,
    viewModel: LogViewModel = viewModel()
) {
    val threats by viewModel.threats.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new threats arrive
    LaunchedEffect(threats.size) {
        if (threats.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.log_title), 
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back), tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearLogs() }) {
                        Icon(Icons.Rounded.DeleteSweep, contentDescription = stringResource(R.string.clear_logs), tint = NeonRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Terminal Header
            TerminalHeader()
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(threats) { threat ->
                    TerminalThreatEntry(threat)
                }
            }
        }
    }
}

@Composable
fun TerminalHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(NeonRed, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonYellow, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonGreen, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text("SEC_TERMINAL_V1.2", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TerminalThreatEntry(threat: Threat) {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(threat.timestamp))

    // We keep the functional colors (Yellow/Red) even in Light mode as they indicate status,
    // but we use slightly deeper shades in the theme definition already.
    val (tag, color) = when {
        threat.threatType.contains("Scan", ignoreCase = true) -> "[WARN]" to MaterialTheme.colorScheme.tertiary
        threat.threatType.contains("Intrusion", ignoreCase = true) -> "[ERR!]" to MaterialTheme.colorScheme.error
        threat.threatType.contains("Status", ignoreCase = true) -> "[INFO]" to MaterialTheme.colorScheme.primary
        else -> "[OK  ]" to MaterialTheme.colorScheme.secondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row {
            Text(
                text = "$dateString ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "$tag ",
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = threat.threatType,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = " > SRC: ${threat.ipAddress} | PORT: ${threat.port} | NET: ${threat.networkName}",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), thickness = 0.5.dp)
    }
}package com.example.waypointv12.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waypointv12.R
import com.example.waypointv12.data.Threat
import com.example.waypointv12.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    onNavigateBack: () -> Unit,
    viewModel: LogViewModel = viewModel()
) {
    val threats by viewModel.threats.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new threats arrive
    LaunchedEffect(threats.size) {
        if (threats.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.log_title), 
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back), tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearLogs() }) {
                        Icon(Icons.Rounded.DeleteSweep, contentDescription = stringResource(R.string.clear_logs), tint = NeonRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Terminal Header
            TerminalHeader()
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(threats) { threat ->
                    TerminalThreatEntry(threat)
                }
            }
        }
    }
}

@Composable
fun TerminalHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(NeonRed, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonYellow, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(8.dp).background(NeonGreen, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text("SEC_TERMINAL_V1.2", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TerminalThreatEntry(threat: Threat) {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(threat.timestamp))

    // We keep the functional colors (Yellow/Red) even in Light mode as they indicate status,
    // but we use slightly deeper shades in the theme definition already.
    val (tag, color) = when {
        threat.threatType.contains("Scan", ignoreCase = true) -> "[WARN]" to MaterialTheme.colorScheme.tertiary
        threat.threatType.contains("Intrusion", ignoreCase = true) -> "[ERR!]" to MaterialTheme.colorScheme.error
        threat.threatType.contains("Status", ignoreCase = true) -> "[INFO]" to MaterialTheme.colorScheme.primary
        else -> "[OK  ]" to MaterialTheme.colorScheme.secondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row {
            Text(
                text = "$dateString ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "$tag ",
                color = color,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = threat.threatType,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = " > SRC: ${threat.ipAddress} | PORT: ${threat.port} | NET: ${threat.networkName}",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), thickness = 0.5.dp)
    }
}
