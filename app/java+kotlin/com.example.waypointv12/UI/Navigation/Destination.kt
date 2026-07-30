package com.example.waypointv12.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Dashboard : Destination

    @Serializable
    data object Menu : Destination

    @Serializable
    data object ThreatLog : Destination

    @Serializable
    data object Settings : Destination
}
