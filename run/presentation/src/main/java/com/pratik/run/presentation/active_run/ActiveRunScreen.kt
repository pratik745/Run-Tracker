@file:OptIn(ExperimentalMaterial3Api::class)

package com.pratik.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pratik.core.presentation.designsystem.RuniqueTheme
import com.pratik.core.presentation.designsystem.StartIcon
import com.pratik.core.presentation.designsystem.StopIcon
import com.pratik.core.presentation.designsystem.components.RuniqueActionButton
import com.pratik.core.presentation.designsystem.components.RuniqueDialog
import com.pratik.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.pratik.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.pratik.core.presentation.designsystem.components.RuniqueScaffold
import com.pratik.core.presentation.designsystem.components.RuniqueToolbar
import com.pratik.core.presentation.ui.ObserveAsEvent
import com.pratik.run.presentation.R
import com.pratik.run.presentation.active_run.components.RunDataCard
import com.pratik.run.presentation.active_run.service.ActiveRunService
import com.pratik.run.presentation.maps.TrackerMap
import com.pratik.run.presentation.util.hasLocationPermission
import com.pratik.run.presentation.util.hasPostNotificationPermission
import com.pratik.run.presentation.util.shouldShowLocationPermissionRationale
import com.pratik.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ActiveRunScreenRoot(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit
) {
    val viewModel: ActiveRunViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    val context = LocalContext.current
    ObserveAsEvent(flow = viewModel.events) { event ->
        when(event) {
            is ActiveRunEvents.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            ActiveRunEvents.RunSaved -> onFinish()
        }
    }

    ActiveRunScreen(
        state = state,
        onServiceToggle = onServiceToggle,
        onAction = { action ->
            when(action) {
                is ActiveRunAction.OnBackClick -> {
                    if(!viewModel.state.value.hasStartedRunning) {
                        onBack()
                    }
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasCoarseLocationPermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true
        submitPermissionInfo(
            context = context,
            hasLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
            hasNotificationPermission = hasNotificationPermission,
            onAction = onAction
        )
    }

    LaunchedEffect(key1 = true) {
        submitPermissionInfo(
            context = context,
            hasLocationPermission = context.hasLocationPermission(),
            hasNotificationPermission = context.hasPostNotificationPermission(),
            onAction = onAction
        )
        val activity = context as ComponentActivity
        if(
            !activity.shouldShowLocationPermissionRationale() &&
            !activity.shouldShowNotificationPermissionRationale()
        ) {
            permissionLauncher.requestRuniquePermissions(context)
        }
    }

    LaunchedEffect(key1 = state.shouldTrack) {
        if(context.hasLocationPermission() && state.shouldTrack && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }
    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if( state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    RuniqueScaffold(
        withGradient = false,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = { onAction(ActiveRunAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = if(state.shouldTrack) StopIcon else StartIcon,
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if(state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                }else{
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcessed(stream.toByteArray()))
                },
                modifier = Modifier
                    .fillMaxSize()
            )

           RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .fillMaxWidth()
            )
        }
    }
    if(!state.shouldTrack && state.hasStartedRunning) {
       RuniqueDialog(
           title = stringResource(id = R.string.runnique_is_paused),
           onDismiss = {
               onAction(ActiveRunAction.OnResumeRunClick)
           },
           description = stringResource(id = R.string.resume_or_finish_run),
           primaryButton = {
               RuniqueActionButton(
                   text = stringResource(id = R.string.resume),
                   isLoading = false,
                   onClick = {
                       onAction(ActiveRunAction.OnResumeRunClick)
                   },
                   modifier = Modifier.weight(1f)
               )
           },
           secondaryButton = {
               RuniqueOutlinedActionButton(
                   text = stringResource(id = R.string.finish),
                   isLoading = state.isSavingRun,
                   onClick = {
                       onAction(ActiveRunAction.OnFinishRunClick)
                   },
                   modifier = Modifier.weight(1f)
               )
           }
       )
    }
    if(state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = { /* Normal dismissing not allowed for permissions */ },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }
                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }
                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context)
                    }
                )
            }
        )
    }
}


private fun submitPermissionInfo(
    context: Context,
    hasLocationPermission: Boolean,
    hasNotificationPermission: Boolean,
    onAction: (ActiveRunAction) -> Unit
) {
    val activity = context as ComponentActivity
    onAction(
        ActiveRunAction.SubmitLocationPermissionInfo(
            acceptedLocationPermission = hasLocationPermission,
            showLocationRationale = activity.shouldShowLocationPermissionRationale()
        )
    )

    onAction(
        ActiveRunAction.SubmitNotificationPermissionInfo(
            acceptedNotificationPermission = hasNotificationPermission,
            showNotificationRationale = activity.shouldShowNotificationPermissionRationale()
        )
    )
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(
    context: Context
) {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val notificationPermission = if(Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !context.hasLocationPermission() && !context.hasPostNotificationPermission() -> {
            launch(locationPermissions + notificationPermission)
        }
        !context.hasLocationPermission() -> {
            launch(locationPermissions)
        }
        !context.hasPostNotificationPermission() -> {
            launch(notificationPermission)
        }
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onServiceToggle = {},
            onAction = {}
        )
    }
}