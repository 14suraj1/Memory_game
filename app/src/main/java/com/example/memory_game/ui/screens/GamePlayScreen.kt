package com.example.memory_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import com.example.memory_game.ui.components.*
import com.example.memory_game.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePlayScreen(
    onBack: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onFinish: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val scoreManager = remember { ScoreManager(context) }
    
    val sentences = listOf(
        "The future belongs to those who believe in the beauty of their dreams",
        "Your limitation is only your imagination",
        "Push yourself, because no one else is going to do it for you",
        "Great things never come from comfort zones",
        "Dream it. Wish it. Do it.",
        "Success doesn't just find you. You have to go out and get it.",
        "The harder you work for something, the greater you'll feel when you achieve it.",
        "Dream bigger. Do bigger.",
        "Don't stop when you're tired. Stop when you're done.",
        "Wake up with determination. Go to bed with satisfaction.",
        "Do something today that your future self will thank you for.",
        "Little things make big days.",
        "It's going to be hard, but hard does not mean impossible.",
        "Don't wait for opportunity. Create it.",
        "Sometimes we're tested not to show our weaknesses, but to discover our strengths.",
        "The key to success is to focus on goals, not obstacles.",
        "Dream it. Believe it. Build it.",
        "Everything you can imagine is real",
        "What you do today can improve all your tomorrows",
        "Don't be afraid to give up the good to go for the great"
    )

    var gameState by remember { mutableStateOf("READY") } // READY, MEMORIZING, TYPING, FINISHED
    var score by remember { mutableIntStateOf(0) }
    var round by remember { mutableIntStateOf(1) }
    var textToMemorize by remember { mutableStateOf(sentences.random()) }
    var typedText by remember { mutableStateOf("") }
    var timerValue by remember { mutableIntStateOf(0) }

    // Timer Settings
    var isMemorizeUnlimited by remember { mutableStateOf(false) }
    var isTypingUnlimited by remember { mutableStateOf(false) }

    LaunchedEffect(gameState, isMemorizeUnlimited, isTypingUnlimited) {
        when (gameState) {
            "MEMORIZING" -> {
                if (!isMemorizeUnlimited) {
                    timerValue = 5
                    while (timerValue > 0) {
                        delay(1000)
                        timerValue--
                    }
                    gameState = "TYPING"
                }
            }
            "TYPING" -> {
                if (!isTypingUnlimited) {
                    timerValue = 30
                    while (timerValue > 0) {
                        delay(1000)
                        timerValue--
                    }
                    gameState = "FINISHED"
                }
            }
            "FINISHED" -> {
                // Save score to ScoreManager
                scoreManager.saveScore("Player $round", score)
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Game Play", 
                        modifier = Modifier.fillMaxWidth(), 
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        score = 0
                        round = 1
                        typedText = ""
                        textToMemorize = sentences.random()
                        gameState = "READY"
                    }) {
                        Icon(Icons.Default.Refresh, "Reset")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BackgroundStart, BackgroundEnd)))
        ) {
            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Score: $score", fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🚩", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Round: $round", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (gameState) {
                "READY" -> ReadyToPlayContent(
                    onStart = { gameState = "MEMORIZING" }, 
                    round = round,
                    isMemorizeUnlimited = isMemorizeUnlimited,
                    onMemorizeUnlimitedChange = { isMemorizeUnlimited = it },
                    isTypingUnlimited = isTypingUnlimited,
                    onTypingUnlimitedChange = { isTypingUnlimited = it }
                )
                "MEMORIZING" -> MemorizeContent(
                    text = textToMemorize,
                    timerValue = timerValue,
                    isUnlimited = isMemorizeUnlimited,
                    onTimeUp = { gameState = "TYPING" }
                )
                "TYPING" -> TypingContent(
                    timerValue = timerValue,
                    isUnlimited = isTypingUnlimited,
                    onCheckAnswer = { 
                        gameState = "FINISHED" 
                    },
                    text = typedText,
                    onValueChange = { typedText = it }
                )
                "FINISHED" -> ResultContent(
                    originalText = textToMemorize,
                    typedText = typedText,
                    totalScore = score,
                    onNextRound = {
                        // Calculate points earned this round and add to total score
                        val accuracy = calculateAccuracy(textToMemorize, typedText)
                        val pointsEarned = (accuracy * 10).toInt()
                        score += pointsEarned
                        round++
                        typedText = ""
                        
                        // Pick a new sentence that is different from the current one
                        var nextSentence = sentences.random()
                        while (nextSentence == textToMemorize) {
                            nextSentence = sentences.random()
                        }
                        textToMemorize = nextSentence

                        gameState = "READY"
                    }
                )
            }
        }
    }
}

@Composable
fun ReadyToPlayContent(
    onStart: () -> Unit, 
    round: Int,
    isMemorizeUnlimited: Boolean,
    onMemorizeUnlimitedChange: (Boolean) -> Unit,
    isTypingUnlimited: Boolean,
    onTypingUnlimitedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Play Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(PrimaryGradientStart, PrimaryGradientEnd)),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("▶", fontSize = 40.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Ready to Play?", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextTitle)
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Memorize the text that appears, then type it correctly to earn points!",
            fontSize = 16.sp,
            color = TextBody,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timer Settings Cards
        TimerModeSelector(
            title = "Memorize Timer", 
            option1 = "5 Seconds", 
            option2 = "Unlimited",
            isUnlimited = isMemorizeUnlimited,
            onUnlimitedChange = onMemorizeUnlimitedChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        TimerModeSelector(
            title = "Typing Timer", 
            option1 = "30 Seconds", 
            option2 = "Unlimited",
            isUnlimited = isTypingUnlimited,
            onUnlimitedChange = onTypingUnlimitedChange
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryGradientButton(text = "Start Round $round", onClick = onStart)
    }
}

@Composable
fun TimerModeSelector(
    title: String, 
    option1: String, 
    option2: String, 
    isUnlimited: Boolean, 
    onUnlimitedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = TextBody)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(20.dp)).padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            brush = if (!isUnlimited) Brush.horizontalGradient(listOf(PrimaryGradientStart, PrimaryGradientEnd)) 
                                    else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onUnlimitedChange(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        option1, 
                        color = if (!isUnlimited) Color.White else TextBody, 
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            brush = if (isUnlimited) Brush.horizontalGradient(listOf(PrimaryGradientStart, PrimaryGradientEnd)) 
                                    else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onUnlimitedChange(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        option2, 
                        color = if (isUnlimited) Color.White else TextBody, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MemorizeContent(text: String, timerValue: Int, isUnlimited: Boolean, onTimeUp: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Timer display
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(if (isUnlimited) "∞" else "🕒", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isUnlimited) "Unlimited" else "Memorize: ${timerValue}s", 
                    fontWeight = FontWeight.Bold, 
                    color = TextTitle
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(32.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = TextTitle
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("👁️", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Focus and remember!", color = TextBody)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        PrimaryGradientButton(text = "Ready to Type", onClick = onTimeUp)
    }
}

@Composable
fun TypingContent(timerValue: Int, isUnlimited: Boolean, onCheckAnswer: () -> Unit, text: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Type what you remember", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextTitle)
        Text("Be as accurate as possible!", color = TextBody)

        Spacer(modifier = Modifier.height(24.dp))

        // Remaining timer
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(if (isUnlimited) "∞" else "🕒", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isUnlimited) "Unlimited" else "${timerValue}s remaining", 
                    fontWeight = FontWeight.Bold, 
                    color = WarningOrange
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text("Type the text here...") },
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = PrimaryGradientStart.copy(alpha = 0.5f),
                focusedIndicatorColor = PrimaryGradientStart
            )
        )
        
        Text(
            "${text.length}/500 characters", 
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp), 
            textAlign = TextAlign.End,
            color = TextBody,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryGradientButton(text = "Check Answer", onClick = onCheckAnswer)
    }
}

@Composable
fun ResultContent(
    originalText: String, 
    typedText: String, 
    totalScore: Int,
    onNextRound: () -> Unit
) {
    val accuracy = calculateAccuracy(originalText, typedText)
    val pointsEarned = (accuracy * 10).toInt()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        if (accuracy > 0.8) listOf(SuccessGreen.copy(0.8f), Color.Green)
                        else listOf(ErrorRed.copy(0.8f), Pink40)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(if (accuracy > 0.8) "✓" else "✕", fontSize = 40.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            if (accuracy > 0.8) "Well Done!" else "Not Quite Right", 
            fontSize = 28.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = TextTitle
        )
        Text(
            if (accuracy > 0.8) "Great memory skills!" else "Keep practicing to improve!", 
            color = TextBody
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ResultRow("Accuracy", "${(accuracy * 100).toInt()}%", PrimaryGradientEnd)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ResultRow("Points Earned", pointsEarned.toString(), if (pointsEarned > 0) SuccessGreen else ErrorRed)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ResultRow("Total Score", totalScore.toString(), WarningOrange)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Original Text:", fontWeight = FontWeight.Bold, color = TextTitle)
                Text(originalText, color = TextBody)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Answer:", fontWeight = FontWeight.Bold, color = TextTitle)
                Text(typedText.ifEmpty { "(No answer)" }, color = TextBody)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        PrimaryGradientButton(text = "Try Again", onClick = onNextRound)
    }
}

@Composable
fun ResultRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextBody)
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 24.sp)
    }
}

private fun calculateAccuracy(original: String, typed: String): Float {
    if (original.isEmpty()) return 0f
    if (typed.isEmpty()) return 0f
    val commonChars = original.zip(typed).count { it.first.lowercaseChar() == it.second.lowercaseChar() }
    return commonChars.toFloat() / max(original.length, typed.length)
}
