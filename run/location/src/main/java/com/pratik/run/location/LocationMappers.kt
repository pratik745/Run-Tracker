package com.pratik.run.location

import android.location.Location
import com.pratik.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.pratik.core.domain.location.Location(
            long = longitude,
            lat = latitude
        ),
        altitude = altitude
    )
}