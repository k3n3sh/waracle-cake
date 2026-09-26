package com.waracle.cakes.presentation.cakelist

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.presentation.isReduceMotionEnabled
import com.waracle.cakes.resources.Res
import com.waracle.cakes.resources.app_title
import com.waracle.cakes.resources.loading_cakes
import com.waracle.cakes.resources.no_cakes
import com.waracle.cakes.resources.refresh
import com.waracle.cakes.resources.retry
import com.waracle.cakes.resources.show_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar, PullToRefreshBox
@Composable
fun CakeListScreen(viewModel: CakeListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.app_title), Modifier.semantics { heading() })
                },
                actions = {
                    // for people who can't pull to refresh
                    IconButton(onClick = viewModel::loadCakes, enabled = !state.isLoading) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(Res.string.refresh))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading && state.cakes.isNotEmpty(),
            onRefresh = viewModel::loadCakes,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val error = state.error
            when {
                state.cakes.isNotEmpty() -> LazyColumn(Modifier.fillMaxSize()) {
                    // refresh failed: keep the list, show the error on top
                    if (error != null) {
                        item {
                            ListItem(
                                headlineContent = { Text(error.message()) },
                                trailingContent = {
                                    TextButton(onClick = viewModel::loadCakes) { Text(stringResource(Res.string.retry)) }
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    headlineColor = MaterialTheme.colorScheme.onErrorContainer,
                                ),
                                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                            )
                        }
                    }
                    // titles are unique after de-duplication
                    itemsIndexed(state.cakes, key = { _, cake -> cake.title }) { index, cake ->
                        FadeIn(index, state.loadCount, Modifier.animateItem()) {
                            CakeRow(cake, onClick = { viewModel.selectCake(cake) })
                            if (index < state.cakes.lastIndex) HorizontalDivider()
                        }
                    }
                }

                state.isLoading -> {
                    val loading = stringResource(Res.string.loading_cakes)
                    CircularProgressIndicator(
                        Modifier
                            .align(Alignment.Center)
                            .semantics {
                                contentDescription = loading
                                liveRegion = LiveRegionMode.Polite
                            },
                    )
                }

                error != null -> Message(
                    text = error.message(),
                    buttonText = stringResource(Res.string.retry),
                    onClick = viewModel::loadCakes,
                )

                else -> Message(
                    text = stringResource(Res.string.no_cakes),
                    buttonText = stringResource(Res.string.refresh),
                    onClick = viewModel::loadCakes,
                )
            }
        }
    }

    state.selectedCake?.let { cake ->
        CakeDetailDialog(cake, onDismiss = { viewModel.selectCake(null) })
    }

    // TODO: grid / list-detail layout for tablets
}

@Composable
private fun CakeRow(cake: Cake, onClick: () -> Unit) {
    val clickLabel = stringResource(Res.string.show_description)
    ListItem(
        headlineContent = { Text(cake.title, style = MaterialTheme.typography.titleMedium) },
        leadingContent = {
            CakeImage(
                url = cake.imageUrl,
                contentDescription = null, // title is right next to it
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier.clickable(onClickLabel = clickLabel, onClick = onClick),
    )
}

@Composable
private fun Message(text: String, buttonText: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                // read out by screen readers when it appears
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
            Button(onClick = onClick) { Text(buttonText) }
        }
    }
}

// Fade + drop in, first few items staggered.
// Replays on each new load, but not after rotation or when scrolling back.
@Composable
private fun FadeIn(index: Int, loadCount: Int, modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    var shown by rememberSaveable(loadCount) { mutableStateOf(false) }
    val skip = shown || isReduceMotionEnabled()
    val progress = remember(loadCount) { Animatable(if (skip) 1f else 0f) }
    LaunchedEffect(loadCount) {
        if (!skip) {
            progress.animateTo(1f, tween(durationMillis = 600, delayMillis = if (index < 8) index * 100 else 0))
        }
        shown = true
    }
    Column(
        modifier = modifier.graphicsLayer {
            alpha = progress.value
            translationY = (1f - progress.value) * -80.dp.toPx()
        },
        content = content,
    )
}
