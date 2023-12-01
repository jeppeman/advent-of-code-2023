# Weather app

This is a simple application that displays the weather of a device location, i.e. the location that
a device which runs the application is in.

The flow of the app can be described as follows:

1) Are location updates enabled on the device?<br/>
   |__ Yes -> Go to 2)<br/>
   |__ No &nbsp;-> Prompt user to turn on location updates on the device. The app will then listen
   for location availability change broadcasts, and act accordingly, such that when
   when the user returns to the app, we go to 2) automatically.
2) Has the user granted permissions to the app for querying the device location?<br/>
   |__ Yes -> Go to 3)<br/>
   |__ No  -> Prompt user to grant location permissions to the app.<br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|__ If granted     -> Go to 3) <br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|__ If not granted -> Return to 2) <br/>
3) Get the geolocation of the device<br/>
   |__ Success -> Go to 4)<br/>
   |__ Fail    -> Notify user that we couldn't the device location, show a retry button.<br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|__ On retry -> Return to 3)
   
4) Based on the location from 3), make a request to https://api.openweathermap.org/data/2.5/ to get the current weather. <br/>
   |__ Success -> Display the weather to the user.<br/>
   |__ Fail    -> Notify user that we failed to get the weather, show a retry button.<br/>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|__ On retry -> Return to 4)


### Demo run
https://user-images.githubusercontent.com/5256210/208458427-15fcafc4-c084-4060-a4c7-b78e0d2fb4eb.mp4

### Build and run

The app can be built and run in the following ways:
* Run the `app`-module from Android Studio.
* Run `./gradlew installDebug` from the root folder with an Android device hooked up to adb.

### Testing
The project contains two types of tests:
* Unit tests that run on the JVM, these can be run with `./gradlew testDebug`
* Runtime agnostic and hermetic E2E tests. These are acceptance tests that test the whole system with mocked backend
  responses, the tests can be run both on JVM and instrumented on device, so both `./gradlew testDebug`
  and `./gradlew connectedDebugAndroidTest` works.

### Architecture 
For the UI layer the project uses Jetpack Compose along with a pattern akin to a simplified version of MVI 
(basically [Elm-architecture](https://guide.elm-lang.org/architecture/)), with a twist: the presenter (ViewModel) 
produces its state with a @Composable function that gets converted into a `StateFlow`, you can read more about this 
[here](https://github.com/cashapp/molecule), but the gist of this pattern is that it allows us to write presenter logic 
in a very concise manner by leveraging the powers of composable functions; once you're familiar with the concept of 
composable functions, it is also, in my opinion, less cognitive overhead than doing same thing with `Flow`s. The main 
drawback of this approach is that Molecule is still in a nascent stage, and may therefore be unstable and less widely
used in the broader community.

Apart from that, on a higher level, the project is modularised on a feature and library basis. What this means is that 
business level features (such as the weather screen) get packaged into isolated and self-contained modules called 
**features**; and other pieces of reusable and related functionality (such as geolocation functionality) gets packages into 
modules called **libraries**. This gives us a very low degree of coupling and high degree of maintainability. 
On a larger scale project we would normally also extract these into entirely separate Gradle-modules as well, I haven't
done that here given the small scale of the app as well as in the interest of time.