###  movieDB
We use Retrofit to consume the MovieDb Api. We choose the MVVM Architechure with ViewModel, LiveData/Flow, Coroutines and Room Database.
For Dependency Injection we are using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

The navigation is handle by [navigation component](https://developer.android.com/guide/navigation/navigation-getting-started).

Migration to [Jetpack compose](https://developer.android.com/jetpack/compose)

We have the Home activity where the user can find any movie or tv-series by typing at the search editext.

If you like to run this project, you have to get an api key from https://developers.themoviedb.org/3/getting-started/introduction
and replace at Definitions class the variable API_KEY with your api key.

