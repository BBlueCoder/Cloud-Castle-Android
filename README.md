<h1 align="left"><img src="app/src/main/ic_launcher-playstore.png" width="24" /> Cloud Castle Android</h1>

![Android](https://img.shields.io/badge/android-green)
![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-purple)
![minSdk](https://img.shields.io/badge/minSdk-21-green)
![targetSdk](https://img.shields.io/badge/targetSdk-34-blue)
![Material Design](https://img.shields.io/badge/Material_Design-3-pink)

Cloud Castle is an Android app that connects to the [`Cloud-Castle-API`](https://github.com/BBlueCoder/Cloud-Castle-API), providing efficient file retrieval and management capabilities. This app serves as a full-fledged cloud storage solution, developed using the latest Android development practices and trends.

Key Features: <br>
📂 **File Management**: Easily browse, search, and organize your files with an intuitive user interface. <br>
📤 **File Uploads**: Upload new files directly from your smartphone, making sharing and storage a breeze. <br>
☁️ **Cloud Storage**: Experience the convenience of cloud storage with secure access to your data at all times. <br>

What Sets Cloud Castle Apart: <br>
🚀 **Top-Tier Android Development**: Crafted following the latest trends and best practices in Android development. <br>
🔧 **Advanced Libraries**: Implementation of various Android Jetpack libraries for optimal performance. <br>
🏛️ **Clean Architectures and SOLID Principles**: Ensuring robust, maintainable code that evolves with your needs. <br>
🔍 **Scalable and Testable**: A future-proof design that can grow with your demands and is easily testable. <br>
🔄 **Continuous Integration**: Utilizes GitHub Actions for seamless updates and reliable performance. <br>
📆 **Always Up-to-Date**: Count on us to keep Cloud Castle maintained and up-to-date, adapting to the ever-changing Android ecosystem. <br>

## Application's UI
Cloud Castle's UI is simple and elegant. The app's UI is built using [jetpack compose](https://developer.android.com/jetpack/compose) and [Material Design 3](https://m3.material.io/). For navigation it implements the [Navigation component](https://developer.android.com/jetpack/compose/navigation) library for `jetpack compose`.

![App](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/resources/cloud_castle.gif)

<p>
  <img src="resources/Screenshot_3.png" width="250" />
  <img src="resources/Screenshot_2.png" width="250" />
  <img src="resources/Screenshot_1.png" width="250" />
</p>

## Tech-Stack
Cloud Castle implements the latest trend libraries and best practices in android ecosystem. 

* Tech-Stack
  *  [Kotlin](https://developer.android.com/kotlin/first)
  *  [Jetpack Compose](https://developer.android.com/jetpack/compose)
  *  [Coroutines](https://developer.android.com/kotlin/coroutines)
  *  [Flow](https://developer.android.com/kotlin/flow)
  *  [WorkManager](https://developer.android.com/guide/background/persistent/getting-started)
  *  [Navigating with Compose](https://developer.android.com/jetpack/compose/navigation)
  *  [Material Design 3](https://m3.material.io/)
  *  [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
  *  [Retrofit](https://square.github.io/retrofit/)
  *  [Coil](https://github.com/coil-kt/coil)
  *  [Media3](https://developer.android.com/guide/topics/media/media3)
  *  [Mockk](https://mockk.io/)
  *  [JUnit4](https://junit.org/junit4/)
  *  [Konsist](https://docs.konsist.lemonappdev.com/)
 
## Project's Structure
All the packages reside in the app package except `konsist_test` package. 

![App structure](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/resources/app_structure.png)

The `data` and `ui` packages are layer components of `clean architecture`, the other 3 packages are non-layer components:
*  **di**: This package contains a [Hilt Module](https://developer.android.com/training/dependency-injection/hilt-android#hilt-modules) and other classes that are used in the hilt module.
*  **utils**: This package contains differents classes and objects that are used in the app.
*  **worker**: This package contains a [WorkManager](https://developer.android.com/guide/background/persistent/getting-started#define_the_work) class.

## Application's Architecture
Cloud Castle implements `MVVM Architecure` the [recommended app architecture](https://developer.android.com/topic/architecture#mobile-app-ux) from Android Developers team and `SOLID principles`, it is scalable and easy to test

![App Architecture](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/resources/clean_architecture.png) 

The `data` and `ui` packages that mentioned above are layers of the MVVM architecture:
*  **data**: The data layer of an app contains the business logic. It is responsible for fetching data from the api through the repositories classes.
*  **ui**:  UI layer is responsible for displaying the data on the screen, it contains screens and `viewmodels`.

## CI
This projects uses [Github Actions](https://github.com/features/actions) to create a CI pipeline. With each push or pull request to the `main` branch the github actions run the workflow localed in [./github/workflows](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/.github/workflows/main.yml)
The workflow contains various jobs:
*  `lint`: Checks project source files for potential bugs and optimization improvements for correctness, security, performance, usability, accessibility, and internationalization.
*  `konsist`: Konsist is a powerful static code analyzer tailored for Kotlin, focused on ensuring codebase consistency and adherence to coding conventions.
*  `unit-test`: Run unit tests.
*  `instrumentation-test`: Run instrumentation tests.
*  `build-debug-apk`: Build a debug apk.

## API Authentication
The Cloud Castle API endpoints require an authentication token for the user, the login and signup endpoints return a token that expires in 35 minutes. To improve the user experience the app's implements a mechanism 
to prevent the user login every 35 minutes as long as the app didn't close. There are two http interceptors, [`RequestInterceptor`](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/app/src/main/java/com/bluecoder/cloudcastle/di/RequestInterceptor.kt)
and [`ResponseInterceptor`](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/app/src/main/java/com/bluecoder/cloudcastle/di/ResponseInterceptor.kt)

![Http interceptors](https://github.com/BBlueCoder/Cloud-Castle-Android/blob/master/resources/http_interceptors.png)

*  `RequestInterceptor`: Intercepts every http request and add a token to the headers of the request.
*  `ResponseInterceptor`: Intercepts every response, if the response code is 401 that means the token is expired, log the user in again and resend the request with the new token.

## Feedback
We highly value your feedback as it helps us continually improve ViDown. Please don't hesitate to reach out to us with your comments, suggestions, or any issues you encounter while using the app.

## Contributing
Contributions are always welcome!
