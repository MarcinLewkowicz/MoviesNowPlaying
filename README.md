# Movies Now Playing Demo

This project was created for demonstration purposes only.

### Architecture

-   Kotlin
-   Single Activity
-   Android View (xml layouts), because of View Bindings usage requirement
-   Jetpack Navigation + SafeArgs
-   Dependency injection: Hilt
-   Coroutines
-   View model (with Flow)
-   Networking: OkHttp, Retrofit, Coil

### Remarks

- The ApiKey to the The Movie DB API was extracted outside of this repository. It was not absolutely neccessry, but I wanted to show how would I do it in a real project. The ApiKey was delivered in secret.properties file by the email but anyone can create it on its own on the The Movie DB web page. The file contains `apiKey` property and should be attached to the root dir. Alternatively the environment variable MOVIES_NOW_PLAYING_API_KEY may be used to define that key.
- The search function was realised as a feature on the main app screen (movies list). The autocomplete feature is not likely to be realised easily because there is no API request to get all movies titles. Instead, the search works on-the-fly when user is typing the search query in the search view and the matching items are shown on the screen (instead of the main/regular movies list).
- I was thinking about adding Favorites screen, but it was not in requirements and realising it would require fetching details about favorites movies one by one from API or store movie details locally. Both solutions have some drawback and finally I decided to skip it for now.
