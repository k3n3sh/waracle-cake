# Waracle Cakes

Compose Multiplatform app (Android + iOS) that loads the Waracle cake list, removes duplicates,
sorts it by name and shows it.

## Running

You need JDK 17+, a recent Android Studio and Android SDK 36. Xcode 16+ for iOS.

- **Android:** open the project in Android Studio and run `composeApp`, or `./gradlew :composeApp:installDebug`
- **iOS:** open `iosApp/iosApp.xcodeproj` and run. For a real device, set `TEAM_ID` in
  `iosApp/Configuration/Config.xcconfig`.

## Tests

```
./gradlew :composeApp:testDebugUnitTest
./gradlew :composeApp:iosSimulatorArm64Test
```

- `CakeListViewModelTest`: the two required cases (load succeeds: no duplicates, sorted; load fails: error shown)
  and retry after a failure
- `CakeRepositoryImplTest`: JSON mapping, and no request when offline, using Ktor's `MockEngine`

The tests use simple fakes, not a mocking library.

## Structure

```
domain/        Cake, CakeRepository, GetCakesUseCase (dedupe + sort)
data/          Ktor API + DTO, CakeRepositoryImpl (turns failures into DataError)
presentation/  CakeListViewModel, CakeListScreen
di/            Koin module
```

Clean Architecture with MVVM + UDF. The screen observes a single `CakeListUiState` and only calls
`loadCakes()` / `selectCake()` on the ViewModel.

- The list loads once, in the ViewModel, so rotating doesn't reload it. The open cake is kept in `SavedStateHandle`,
  so it also comes back after the system kills the app in the background.
- Before each request the app checks connectivity (`ConnectivityManager` on Android, `NWPathMonitor` on iOS)
  and shows "No internet connection" straight away if offline. Failed requests are still handled.
- If nothing has loaded, errors show full screen with a Retry button. If a refresh fails, the list stays
  and a Retry row appears on top.
- Refresh by pulling down or with the button in the top bar.
- Tapping a cake opens a dialog with its description.
- Items fade and drop in, unless the system "reduce motion" / "remove animations" setting is on.
- Screen readers announce loading and errors (live regions), the title is a heading, and rows say what tapping does.
- All text is in `composeResources/values/strings.xml`. Add a `values-xx` folder to translate.

## Libraries

Ktor, kotlinx.serialization, Coil 3, Koin, the JetBrains Lifecycle ViewModel, and Material 3.

## With more time

These are also marked as TODOs in the code:

- Offline cache of the last response
- Retry with backoff, and auto-retry when back online
- Tablet layout
- UI / screenshot tests
- R8 for release builds
