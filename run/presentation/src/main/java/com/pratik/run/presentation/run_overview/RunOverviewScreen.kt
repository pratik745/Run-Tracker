@file:OptIn(ExperimentalMaterial3Api::class)

package com.pratik.run.presentation.run_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.pratik.core.presentation.designsystem.AnalyticsIcon
import com.pratik.core.presentation.designsystem.LogoIcon
import com.pratik.core.presentation.designsystem.LogoutIcon
import com.pratik.core.presentation.designsystem.RunIcon
import com.pratik.core.presentation.designsystem.RuniqueTheme
import com.pratik.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.pratik.core.presentation.designsystem.components.RuniqueScaffold
import com.pratik.core.presentation.designsystem.components.RuniqueToolbar
import com.pratik.core.presentation.designsystem.components.models.DropdownItems
import com.pratik.run.presentation.R
import com.pratik.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    onStartClick: () -> Unit
) {
    val viewModel: RunOverviewViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    RunOverviewScreen(
        state = state,
        onAction = viewModel::onAction,
        onStartClick = onStartClick
    )
}

@Composable
private fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit,
    onStartClick: () -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)
    RuniqueScaffold(
        withGradient = true,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runique),
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    DropdownItems(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropdownItems(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logout)
                    )
                ),
                onMenuItemClicked = { index ->
                    when(index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnLogoutClick)
                    }
                },
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon ,
                onClick = { onStartClick.invoke() }
            )
        }
    ) { padding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.runs,
                key = { it.id }
            ) {
                RunListItem(
                    runUi = it,
                    onDeleteClick = {
                        onAction(RunOverviewAction.DeleteRun(it))
                    },
                    modifier = Modifier
                        .animateItemPlacement()
                )
            }
        }
    }
}

@Preview
@Composable
private fun RunOverviewPreview() {
    RuniqueTheme {
        RunOverviewScreen(
            state = RunOverviewState(),
            onAction = {},
            onStartClick = {}
        )
    }
}
