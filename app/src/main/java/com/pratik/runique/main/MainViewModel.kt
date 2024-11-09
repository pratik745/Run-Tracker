package com.pratik.runique.main

import androidx.lifecycle.viewModelScope
import com.pratik.auth.presentation.login.LoginEvents
import com.pratik.core.domain.authSession.SessionStorage
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
): BaseViewModel<MainAction,MainState>() {

    override fun initState() = MainState()

   init {
       viewModelScope.launch {
           updateState { it.copy(isCheckingAuth = true) }
           val isLoggedIn = sessionStorage.get() != null
           updateState {
               it.copy(
                   isLoggedIn = isLoggedIn,
                   isCheckingAuth = false
               )
           }
       }
   }
}