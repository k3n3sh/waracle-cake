package com.waracle.cakes.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled

@Composable
actual fun isReduceMotionEnabled(): Boolean {
    return remember { UIAccessibilityIsReduceMotionEnabled() }
}
