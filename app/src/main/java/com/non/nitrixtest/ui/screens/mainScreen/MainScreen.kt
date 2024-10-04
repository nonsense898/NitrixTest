package com.non.nitrixtest.ui.screens.mainScreen

import android.app.Activity
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.non.nitrixtest.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel) {
    val activity = LocalContext.current as Activity

    val movieFetchedList by mainViewModel.movieData.observeAsState(emptyList())
    val movieLocalList by mainViewModel.movies.observeAsState(emptyList())

    val movieList = movieLocalList.ifEmpty { movieFetchedList }

    val errorMessageState = mainViewModel.errorMessage.observeAsState().value

    LaunchedEffect(Unit) {
        if (movieLocalList.isEmpty()) {
            mainViewModel.fetchMovies()
            mainViewModel.insertMovies(movieFetchedList)
        }
    }

    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

    Column(modifier = Modifier.padding(top = 30.dp)) {
        when {
            !errorMessageState.isNullOrEmpty() -> {
                ErrorView(errorMessageState)
            }

            movieList.isEmpty() -> {
                LoadingView()
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(movieList) { index, movie ->
                        MovieItem(
                            movie = movie,
                            onMovieClick = { _ ->
                                mainViewModel.setCurrentIndex(index)
                                navController.navigate("playerScreen")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(error: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

//@Composable
//fun EmptyMoviePlaceholder() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(R.drawable.ic_movie_placeholder),
//            contentDescription = "Empty movie list",
//            modifier = Modifier.size(200.dp),
//            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseSurface)
//        )
//
//        Text(
//            text = "No movies available",
//            fontSize = 24.sp,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//    }
//}

