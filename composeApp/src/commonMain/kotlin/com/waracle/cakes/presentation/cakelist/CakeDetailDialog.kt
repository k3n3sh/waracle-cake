package com.waracle.cakes.presentation.cakelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.waracle.cakes.domain.model.Cake
import com.waracle.cakes.resources.Res
import com.waracle.cakes.resources.close
import com.waracle.cakes.resources.no_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun CakeDetailDialog(cake: Cake, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.close)) } },
        title = { Text(cake.title) },
        text = {
            // scrolls in landscape
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CakeImage(
                    url = cake.imageUrl,
                    contentDescription = cake.title,
                    showMessage = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(MaterialTheme.shapes.medium),
                )
                Text(cake.description.ifBlank { stringResource(Res.string.no_description) })
            }
        },
    )
}
