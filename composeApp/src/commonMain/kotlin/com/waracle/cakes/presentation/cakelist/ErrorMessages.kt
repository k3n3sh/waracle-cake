package com.waracle.cakes.presentation.cakelist

import androidx.compose.runtime.Composable
import com.waracle.cakes.domain.model.DataError
import com.waracle.cakes.resources.Res
import com.waracle.cakes.resources.error_invalid_data
import com.waracle.cakes.resources.error_network
import com.waracle.cakes.resources.error_no_internet
import com.waracle.cakes.resources.error_server
import com.waracle.cakes.resources.error_unknown
import org.jetbrains.compose.resources.stringResource

@Composable
fun DataError.message(): String {
    return when (this) {
        DataError.NoInternet -> stringResource(Res.string.error_no_internet)
        DataError.Network -> stringResource(Res.string.error_network)
        is DataError.Server -> stringResource(Res.string.error_server, statusCode)
        DataError.InvalidData -> stringResource(Res.string.error_invalid_data)
        DataError.Unknown -> stringResource(Res.string.error_unknown)
    }
}
