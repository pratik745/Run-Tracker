package com.pratik.run.presentation.maps

import androidx.compose.ui.graphics.Color
import com.pratik.core.domain.location.Location

data class PolylineUi(
    val location1: Location,
    val location2: Location,
    val color: Color
)
