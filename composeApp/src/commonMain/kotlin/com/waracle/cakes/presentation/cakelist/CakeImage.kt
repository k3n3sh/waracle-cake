package com.waracle.cakes.presentation.cakelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.waracle.cakes.resources.Res
import com.waracle.cakes.resources.retry_image
import org.jetbrains.compose.resources.stringResource

// Grey while loading. If it fails, a refresh icon shows and tapping it tries again.
// showMessage adds visible text under the icon, for big images like the dialog.
@Composable
fun CakeImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    showMessage: Boolean = false,
) {
    var attempt by remember(url) { mutableIntStateOf(0) }
    var failed by remember(url, attempt) { mutableStateOf(false) }

    Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
        key(attempt) { // new attempt = new request
            AsyncImage(
                model = url,
                contentDescription = contentDescription,
                onError = { failed = url != null },
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
        if (failed) {
            val message = stringResource(Res.string.retry_image)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { attempt++ }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = message,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (showMessage) {
                    Text(
                        message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
