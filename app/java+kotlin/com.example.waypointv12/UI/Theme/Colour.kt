package com.example.waypointv12.ui.theme

import androidx.compose.ui.graphics.Color

// HyperSec Neon Palette
val NeonCyan = Color(0xFF00FFFF)
val NeonGreen = Color(0xFF39FF14)
val NeonRed = Color(0xFFFF3131)
val NeonYellow = Color(0xFFFFFF33)
val DarkGrey = Color(0xFF121212)
val NearBlack = Color(0xFF080808)

// Optimized Dark Theme
val PrimaryDark = NeonCyan
val OnPrimaryDark = NearBlack
val PrimaryContainerDark = Color(0xFF003D3D)
val OnPrimaryContainerDark = NeonCyan

val SecondaryDark = NeonGreen
val OnSecondaryDark = NearBlack
val SecondaryContainerDark = Color(0xFF003900)
val OnSecondaryContainerDark = NeonGreen

val TertiaryDark = NeonYellow
val OnTertiaryDark = NearBlack
val TertiaryContainerDark = Color(0xFF333300)
val OnTertiaryContainerDark = NeonYellow

val ErrorDark = NeonRed
val OnErrorDark = NearBlack
val ErrorContainerDark = Color(0xFF3D0000)
val OnErrorContainerDark = NeonRed

val BackgroundDark = NearBlack
val OnBackgroundDark = Color(0xFFE0E0E0)
val SurfaceDark = DarkGrey
val OnSurfaceDark = Color(0xFFE0E0E0)
val SurfaceVariantDark = Color(0xFF1E1E1E)
val OnSurfaceVariantDark = NeonCyan

// Re-engineered Light Theme for High Visibility
// We use deeper versions of neon colors to ensure they don't "vanish" on white
val PrimaryLight = Color(0xFF007A7A) // Deep Teal
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFE0FFFF)
val OnPrimaryContainerLight = Color(0xFF002020)

val SecondaryLight = Color(0xFF008000) // Forest Green
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE8F5E9)
val OnSecondaryContainerLight = Color(0xFF002200)

val TertiaryLight = Color(0xFF7B7B00) // Mustard
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFFDE7)
val OnTertiaryContainerLight = Color(0xFF202000)

val ErrorLight = Color(0xFFB00020)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFDECEA)
val OnErrorContainerLight = Color(0xFF410002)

val BackgroundLight = Color(0xFFF8F9FA) // Slightly off-white to reduce glare
val OnBackgroundLight = Color(0xFF1A1C1E)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF1A1C1E)
val SurfaceVariantLight = Color(0xFFE1E2E4)
val OnSurfaceVariantLight = Color(0xFF44474E)
