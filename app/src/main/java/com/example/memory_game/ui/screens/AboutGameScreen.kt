package com.example.memory_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memory_game.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutGameScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Game", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BackgroundStart, BackgroundEnd)))
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Memory Typing Game",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextTitle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "This game is designed to help you improve both your short-term memory and your typing speed and accuracy.",
                        fontSize = 16.sp,
                        color = TextBody
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "How to Play:",
                        fontWeight = FontWeight.Bold,
                        color = TextTitle
                    )
                    Text(
                        text = "1. Memorize the text shown.\n2. Type it correctly within the time limit.\n3. Earn points based on your accuracy!",
                        fontSize = 16.sp,
                        color = TextBody
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Version 1.0.0",
                        fontSize = 12.sp,
                        color = TextBody.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
