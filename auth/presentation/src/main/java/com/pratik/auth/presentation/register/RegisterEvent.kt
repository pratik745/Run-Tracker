package com.pratik.auth.presentation.register

import com.pratik.core.presentation.ui.UiText

sealed interface RegisterEvent {

    data object RegistrationSuccess : RegisterEvent
    data class Error(val error: UiText) : RegisterEvent
}