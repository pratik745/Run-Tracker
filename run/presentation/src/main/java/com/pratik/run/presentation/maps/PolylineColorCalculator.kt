package com.pratik.run.presentation.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.pratik.core.domain.location.LocationTimeStamp
import kotlin.math.abs

object PolylineColorCalculator {

    private const val MAX_SPEED = 20.0
    private const val MIN_SPEED = 5.0


    fun locationsToColor(location1: LocationTimeStamp, location2: LocationTimeStamp): Color {
        val distanceMeters = location1.location.location.distanceTo(location2.location.location)
        val timeDiff = abs((location2.timeStampDuration - location1.timeStampDuration).inWholeSeconds)
        val speedKmh = (distanceMeters / timeDiff) * 3.6

        return interpolateColor(
            speedKmh = speedKmh
        )
    }

    private fun interpolateColor(speedKmh: Double): Color {
        val ratio = ((speedKmh - MIN_SPEED) / (MAX_SPEED - MIN_SPEED)).coerceIn(0.0..1.0)
        val colorInt = if(ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(Color.Green.toArgb(), Color.Yellow.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(Color.Yellow.toArgb(),  Color.Red.toArgb(), midToEndRatio.toFloat())
        }

        return Color(colorInt)
    }
}