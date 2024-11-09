package com.pratik.auth.presentation.login

import androidx.compose.foundation.text2.input.textAsFlow
import androidx.lifecycle.viewModelScope
import com.pratik.auth.domain.AuthRepository
import com.pratik.auth.domain.UserDataValidator
import com.pratik.auth.presentation.R
import com.pratik.core.domain.util.DataError
import com.pratik.core.domain.util.Result
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import com.pratik.core.presentation.ui.UiText
import com.pratik.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
): BaseViewModel<LoginAction, LoginState>() {

    override fun initState() = LoginState()

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(state.value.email.textAsFlow(),state.value.password.textAsFlow()) { email,password ->
            updateState {
                it.copy(
                    canLogin = userDataValidator.isValidEmail(
                        email = email.toString().trim()
                    ) && password.isNotEmpty()
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onAction(action: LoginAction) {
        when(action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state.value.apply {
                    updateState { it.copy(isPasswordVisible = !isPasswordVisible) }
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            updateState { it.copy(isLoggingIn = true) }
            state.value.apply {
                val result = authRepository.login(
                    email = email.text.toString().trim(),
                    password = password.text.toString().trim()
                )
                updateState { it.copy(isLoggingIn = false) }
                when(result) {
                    is Result.Error -> {
                        val errorMsg = if (result.error == DataError.Network.UNAUTHORIZED) {
                            UiText.StringResource(R.string.error_incorrect_email_password)
                        } else result.error.asUiText()
                        eventChannel.send(LoginEvents.Error(errorMsg))
                    }
                    is Result.Success -> {
                        eventChannel.send(LoginEvents.LoginSuccess)
                    }
                }
            }
        }
    }
}