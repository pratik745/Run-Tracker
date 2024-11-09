package com.pratik.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratik.auth.presentation.R
import com.pratik.core.presentation.designsystem.EmailIcon
import com.pratik.core.presentation.designsystem.Poppins
import com.pratik.core.presentation.designsystem.RuniqueTheme
import com.pratik.core.presentation.designsystem.components.GradientBackground
import com.pratik.core.presentation.designsystem.components.RuniqueActionButton
import com.pratik.core.presentation.designsystem.components.RuniquePasswordTextField
import com.pratik.core.presentation.designsystem.components.RuniqueTextField
import com.pratik.core.presentation.ui.ObserveAsEvent
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onSuccessfulLogin: () -> Unit,
    onSignUpClick: () -> Unit
) {
    ObserveEvents(
        loginEvent = viewModel.events,
        onSuccessfulLogin = onSuccessfulLogin
    )
    LoginScreen(
        state = viewModel.state.collectAsState().value,
        onAction = viewModel::onAction,
        onSignUpClick = onSignUpClick
    )
}

@Composable
private fun ObserveEvents(
    loginEvent: Flow<LoginEvents>,
    onSuccessfulLogin: () -> Unit,
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    ObserveAsEvent(flow = loginEvent) { event ->
        when (event) {
            is LoginEvents.Error -> {
                keyboard?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            LoginEvents.LoginSuccess -> {
                keyboard?.hide()
                Toast.makeText(
                    context,
                    R.string.your_logged_in,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulLogin()
            }
        }
    }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onSignUpClick: () -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hi_there),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = R.string.welcome_login_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            RuniqueTextField(
                state = state.email ,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                startIcon = EmailIcon,
                endIcon = null,
                keyBoardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            RuniquePasswordTextField(
                state = state.password,
                hint = stringResource(id = R.string.enter_password),
                title = stringResource(id = R.string.password) ,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            RuniqueActionButton(
                text = stringResource(id = R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogin && !state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                }
            )


            // Sometimes, Modifier.weight doesn't behave as expected in previews
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ){
                    append(stringResource(id = R.string.dont_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation = stringResource(id = R.string.sign_up)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ){
                        append(stringResource(id = R.string.sign_up))
                    }
                }
            }
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onSignUpClick()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    RuniqueTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            onSignUpClick = {}
        )
    }
}