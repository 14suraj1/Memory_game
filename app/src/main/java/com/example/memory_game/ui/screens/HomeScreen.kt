package com.example.memory_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memory_game.ui.components.*
import com.example.memory_game.ui.theme.*

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onViewScoreboard: () -> Unit,
    onAboutGame: () -> Unit
) {
    BackgroundContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder for MemoryType Logo
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(PrimaryGradientStart, PrimaryGradientEnd)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("M", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MemoryType",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTitle
                )
            }
            IconButton(onClick = { /* Settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextTitle)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Main Icon Card
        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.size(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(PrimaryGradientStart, PrimaryGradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Grid icon placeholder
                Text(
                    "⠿", 
                    fontSize = 80.sp, 
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Memory Typing\nGame",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextTitle,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Challenge your memory and typing skills! Remember the text, type it correctly, and climb the leaderboard.",
            fontSize = 16.sp,
            color = TextBody,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryGradientButton(
            text = "Start Playing",
            onClick = onStartGame
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "View Scoreboard",
            onClick = onViewScoreboard
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "About Game",
            onClick = onAboutGame
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        // Stats at bottom (Image 4)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem("1.2K+", "Players", PrimaryGradientStart)
            StatItem("50K+", "Games Played", PrimaryGradientEnd)
            StatItem("98%", "Satisfaction", WarningOrange)
        }
    }
}

@Composable
fun StatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextBody
        )
    }
}
