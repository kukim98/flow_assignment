package com.kwangeonkim.thoth.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kwangeonkim.thoth.data.remote.naver.NaverBookService
import com.kwangeonkim.thoth.presentation.screen.Screen
import com.kwangeonkim.thoth.presentation.screen.recent_search_screen.composable.RecentSearchScreen
import com.kwangeonkim.thoth.presentation.screen.search_screen.SearchScreen
import com.kwangeonkim.thoth.presentation.theme.ThothTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var naverBookService: NaverBookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThothTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SearchScreen.route,
                    ) {
                        composable(route = Screen.SearchScreen.route) {
                            SearchScreen(
                                navController = navController,
                                savedStateHandle = it.savedStateHandle
                            )
                        }
                        composable(route = Screen.RecentSearchScreen.route) {
                            RecentSearchScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}