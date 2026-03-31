package com.example.memory_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memory_game.ui.screens.AboutGameScreen
import com.example.memory_game.ui.screens.GamePlayScreen
import com.example.memory_game.ui.screens.HomeScreen
import com.example.memory_game.ui.screens.ScoreboardScreen

@Composable
fun MemoryGameNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onStartGame = { navController.navigate("game_play") },
                onViewScoreboard = { navController.navigate("scoreboard") },
                onAboutGame = { navController.navigate("about") }
            )
        }
        composable("game_play") {
            GamePlayScreen(
                onBack = { navController.popBackStack() },
                onFinish = { _, _ -> }
            )
        }
        composable("scoreboard") {
            ScoreboardScreen(onBack = { navController.popBackStack() })
        }
        composable("about") {
            AboutGameScreen(onBack = { navController.popBackStack() })
        }
    }
}
