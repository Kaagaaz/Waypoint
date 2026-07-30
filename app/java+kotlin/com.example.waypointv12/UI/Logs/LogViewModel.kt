package com.example.waypointv12.ui.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.waypointv12.data.AppDatabase
import com.example.waypointv12.data.Threat
import com.example.waypointv12.data.ThreatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ThreatRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ThreatRepository(database.threatDao())
    }

    val threats: StateFlow<List<Threat>> = repository.allThreats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearLogs() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
