package com.pratik.runique.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.pratik.auth.presentation.intro.IntroScreenRoot
import com.pratik.auth.presentation.login.LoginScreenRoot
import com.pratik.auth.presentation.register.RegisterScreenRoot
import com.pratik.run.presentation.active_run.ActiveRunScreenRoot
import com.pratik.run.presentation.active_run.service.ActiveRunService
import com.pratik.run.presentation.run_overview.RunOverviewScreenRoot
import com.pratik.runique.main.MainActivity

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController ,
        startDestination = if(isLoggedIn) Route.RunTracker.RUN else Route.Authentication.AUTH
    ) {
        authGraph(navController)
        runGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Route.Authentication.INTRO,
        route = Route.Authentication.AUTH
    ){
        composable(route = Route.Authentication.INTRO) {
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate(Route.Authentication.REGISTER)
                },
                onSignInClick = {
                    navController.navigate(Route.Authentication.LOGIN)
                }
            )
        }

        composable(route = Route.Authentication.REGISTER) {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Route.Authentication.LOGIN) {
                        popUpTo(Route.Authentication.REGISTER) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Route.Authentication.LOGIN)
                }
            )
        }
        composable(route = Route.Authentication.LOGIN) {
            LoginScreenRoot(
                onSuccessfulLogin = {
                    navController.navigate(Route.RunTracker.RUN) {
                        popUpTo(Route.Authentication.AUTH) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Route.Authentication.REGISTER) {
                        popUpTo(Route.Authentication.LOGIN) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(
        startDestination = Route.RunTracker.RUN_OVERVIEW,
        route = Route.RunTracker.RUN
    ) {
        composable(route = Route.RunTracker.RUN_OVERVIEW) {
            RunOverviewScreenRoot(
                onStartClick = { navController.navigate(route = Route.RunTracker.ACTIVE_RUN) },
                onLogoutClick = {
                    navController.navigate(Route.Authentication.AUTH) {
                        popUpTo(Route.RunTracker.RUN) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Route.RunTracker.ACTIVE_RUN,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = Route.Deeplinks.ACTIVE_RUN_SCREEN
                }
            )
        ) {
            val context = LocalContext.current
            ActiveRunScreenRoot(
                onBack = {
                    navController.navigateUp()
                },
                onFinish = {
                    navController.navigateUp()
                },
                onServiceToggle = { shouldStartService ->
                    if(shouldStartService) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context = context,
                                activityClass = MainActivity::class.java
                            )
                        )
                    } else {
                        context.startService(
                            ActiveRunService.createStopIntent(context)
                        )
                    }
                }
            )
        }
    }
}