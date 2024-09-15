package com.pratik.auth.presentation.register

sealed interface RegisterAction {
    data object onTogglePasswordVisibilityClick : RegisterAction
    data object onLoginClick : RegisterAction
    data object onRegisterClick : RegisterAction
}