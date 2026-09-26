package com.waracle.cakes

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused") // used from Swift
fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
