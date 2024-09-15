package com.pratik.auth.presentation.register

import com.pratik.core.presentation.designsystem.base.BaseViewModel

class RegisterViewModel: BaseViewModel<RegisterAction, RegisterState>() {

    override fun initState(): RegisterState = RegisterState()

    override fun onAction(action: RegisterAction) {

    }
}