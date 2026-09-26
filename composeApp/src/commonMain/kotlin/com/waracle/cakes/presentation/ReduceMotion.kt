package com.waracle.cakes.presentation

import androidx.compose.runtime.Composable

// The system "reduce motion" / "remove animations" setting.
@Composable
expect fun isReduceMotionEnabled(): Boolean
