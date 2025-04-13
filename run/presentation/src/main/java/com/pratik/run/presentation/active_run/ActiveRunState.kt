package com.pratik.run.presentation.active_run

import com.pratik.core.domain.location.Location
import com.pratik.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
  val elapsedTime: Duration = Duration.ZERO,
  val runData: RunData = RunData(),
  val shouldTrack: Boolean = false,
  val hasStartedRunning: Boolean = false,
  val currentLocation: Location? = null,
  val isRunFinished: Boolean = false,
  val isSavingRun: Boolean = false,
  val showNotificationRationale: Boolean = false,
  val showLocationRationale: Boolean = false
)
