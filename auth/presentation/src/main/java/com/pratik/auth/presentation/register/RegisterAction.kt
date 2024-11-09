package com.pratik.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityClick : RegisterAction
    data object OnRegisterClick : RegisterAction
}