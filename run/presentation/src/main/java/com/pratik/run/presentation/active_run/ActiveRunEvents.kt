package com.pratik.run.presentation.active_run

import com.pratik.core.presentation.ui.UiText

sealed interface ActiveRunEvents {

    data class Error(val error: UiText): ActiveRunEvents

    data object RunSaved: ActiveRunEvents
}