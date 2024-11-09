package com.pratik.auth.presentation.login

import com.pratik.core.presentation.ui.UiText

sealed interface LoginEvents {

    data object LoginSuccess : LoginEvents
    data class Error(val error: UiText) : LoginEvents
}