package com.example.memory_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.memory_game.ui.navigation.MemoryGameNavGraph
import com.example.memory_game.ui.theme.Memory_gameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Memory_gameTheme {
                MemoryGameNavGraph()
            }
        }
    }
}
