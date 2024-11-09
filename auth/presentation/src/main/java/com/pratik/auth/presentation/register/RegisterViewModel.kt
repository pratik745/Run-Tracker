package com.pratik.auth.presentation.register

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
): BaseViewModel<RegisterAction, RegisterState>() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        observeCredentialsInput()
    }

    override fun initState(): RegisterState = RegisterState()

    override fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state.value.apply {
                    updateState { it.copy(isPasswordVisible = !isPasswordVisible) }
                }
            }
        }
    }

    private fun observeCredentialsInput(){
        state.value.email.textAsFlow()
            .onEach { email->
                updateState {
                    it.copy(
                        isEmailValid = userDataValidator.isValidEmail(email.toString())
                    )
                }
            }
            .launchIn(viewModelScope)

        state.value.password.textAsFlow()
            .onEach { password->
                updateState {
                    it.copy(
                        passwordValidationState = userDataValidator.validatePassword(password.toString())
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun register() = viewModelScope.launch(Dispatchers.IO) {
        updateState { it.copy(isRegistering = true) }
        state.value.apply {

            val result = authRepository.register(
                email = email.text.toString().trim(),
                password = password.text.toString()
            )

            updateState { it.copy(isRegistering = false) }

            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(
                            RegisterEvent.Error(
                                UiText.StringResource(R.string.email_already_exist)
                            )
                        )
                    } else {
                        eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }
}