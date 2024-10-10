package com.non.nitrixtest

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.non.nitrixtest.ui.screens.mainScreen.MainScreen
import com.non.nitrixtest.ui.screens.noConnectionScreen.NoConnectionScreen
import com.non.nitrixtest.ui.screens.playerScreen.PlayerScreen
import com.non.nitrixtest.ui.theme.NitrixTestTheme
import com.non.nitrixtest.viewmodel.MainViewModel
import com.non.nitrixtest.viewmodel.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkViewModel.startNetworkCallback(this)

        setContent {
            val navController = rememberNavController()
//            val isNetworkAvailable by networkViewModel.isNetworkAvailable.collectAsState()

            NitrixTestTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                ) { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = "mainScreen",
                    ) {

                        composable("mainScreen") { MainScreen(navController, mainViewModel) }
                        composable("playerScreen") { PlayerScreen(navController, mainViewModel) }
//                        composable("noConnectionScreen") { NoConnectionScreen()}
                    }
                }

//                 LaunchedEffect(isNetworkAvailable) {
//                    if (isNetworkAvailable) {
//                        navController.navigate("mainScreen")
//                    } else {
//                        navController.navigate("noConnectionScreen")
//                    }
//                }
            }
        }
    }
}



