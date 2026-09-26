import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // start Koin once
        KoinIosKt.startKoinForIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
