package com.example.memory_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memory_game.ScoreManager
import com.example.memory_game.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scoreManager = remember { ScoreManager(context) }
    val scores = remember { scoreManager.getScores() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scoreboard", fontWeight = FontWeight.Bold) },
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
            if (scores.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No records yet!\nPlay a game to see your score here.",
                        textAlign = TextAlign.Center,
                        color = TextBody,
                        fontSize = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(scores) { index, entry ->
                        ScoreCard(index + 1, entry)
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreCard(rank: Int, entry: ScoreEntry) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (rank <= 3) PrimaryGradientEnd else TextBody,
                modifier = Modifier.width(40.dp)
            )
            
            Text(
                text = entry.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextTitle,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = "${entry.score} pts",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGradientStart
            )
        }
    }
}

data class ScoreEntry(val name: String, val score: Int)
