# Waracle Cakes

Compose Multiplatform app (Android + iOS) that loads the Waracle cake list, removes duplicates,
sorts it by name and shows it.

## Running

Android builds and runs on any machine (Windows, macOS, Linux). iOS is optional and only needs
extra tools if you want to run it.

### Android

Needs Android Studio (recent) with JDK 17+ and Android SDK 36. Nothing else to set up.

1. Clone the repo and open the project folder in Android Studio.
2. Wait for Gradle sync to finish.
3. Pick the `composeApp` run configuration and an emulator or phone, then press Run.

Or from the terminal: `./gradlew :composeApp:installDebug`

### iOS

Needs a Mac with Xcode 16+ (Apple only builds iOS apps on macOS). No Apple account or signing
setup for the simulator.
The first build takes a few minutes while it compiles the shared Kotlin code.

**From Android Studio** (needs the Kotlin Multiplatform plugin: Settings → Plugins):

1. Pick the `iosApp` run configuration.
2. In the device list next to it, pick an iPhone simulator (not a real iPhone).
3. Press Run.

**From Xcode:**

1. Open `iosApp/iosApp.xcodeproj`.
2. Pick an iPhone simulator at the top, then press Run.

Running on a real iPhone is the only case that needs an Apple team: set it under
Signing & Capabilities in Xcode, or `TEAM_ID` in `iosApp/Configuration/Config.xcconfig`.

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


## AI Usage

| Block | Engineer | AI | Lead / Review / Approved |
|---|---|---|---|
| Brief, scope, working rules | Wrote them | Understood context | Engineer |
| Architecture (Clean Architecture, MVVM) | Chose it | Laid out the folders and classes | Engineer |
| Library choices | Picked each one | Gradle setup and fixed setup error | Shared |
| UI theme and branding | Brand colours, trimmed the design | Improved it | Shared |
| API data review | Chose and wrote them | Validated and analysed the data (e.g. photo URL not working) | Shared |
| Error handling (Outcome / DataError) | Chose and wrote them | Tested | Engineer |
| Domain (models, use cases) | Designed and wrote them | Fixed errors | Engineer |
| Data (API, DTOs, mapper, repository) | Designed and wrote them | Fixed errors | Engineer |
| ViewModels and UI state | Wrote them | Fixed errors | Engineer |
| Koin DI | Designed and wrote it | Helped share dependencies on the iOS side | Shared |
| Compose screens | Wrote them | Fixed issues and improved UX | Shared |
| Comments and README | Content direction | Drafted them | AI |
| Test plan (5 tests, naming, simple style) | Set it | Code completion direction | Engineer |
| Writing fakes | Wrote them | - | Engineer |
| Build, lint and unit tests after each step | Ran them | Fixed errors | Engineer |
| Testing on a real device | Did it; found a few issues, including rotation | - | Engineer |

## With more time

These are also marked as TODOs in the code:

- Offline cache of the last response
- Retry with backoff, and auto-retry when back online
- Tablet layout
- More tests: process death, each error type, Compose UI / screenshot tests
- R8 for release builds
