package com.pratik.run.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.pratik.core.domain.location.LocationTimeStamp

@Composable
fun RuniquePolyline(
    locations: List<List<LocationTimeStamp>>
) {
    val polyline = remember(locations) {
        locations.map {
            it.zipWithNext { timestamp1, timestamp2 ->
                PolylineUi(
                    location1 = timestamp1.location.location,
                    location2 = timestamp2.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        location1 = timestamp1,
                        location2 = timestamp2
                    )
                )
            }
        }
    }
    polyline.forEach {
        it.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat,polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat,polylineUi.location2.long),
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}