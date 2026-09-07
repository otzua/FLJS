package com.pikamander2.japanesequizz.features

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.Date
import java.util.Locale

data class Flashcard(
    val id: String, val level: String, val word: String, val reading: String,
    val meaning: String, val exampleSentence: String, val exampleTranslation: String
)

data class CardProgress(
    val id: String,
    var state: String = "new",
    var nextReviewTime: Long = 0L,
    var ease: Float = 2.5f,
    var interval: Int = 0
)

class FlashcardActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.JAPANESE)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                ttsReady = true
            }
        }
    }

    private fun speakWord(text: String) {
        if (ttsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "flashcard_speech")
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val initialLevel = prefs.getString("jlpt_level", "N5") ?: "N5"
        val gson = Gson()
        
        val allBundledCards: List<Flashcard> = try {
            val reader = InputStreamReader(assets.open("flashcards.json"))
            val type = object : TypeToken<List<Flashcard>>() {}.type
            gson.fromJson(reader, type)
        } catch (e: Exception) {
            emptyList()
        }
        
        val progressJson = prefs.getString("flashcard_progress", "{}")
        val progressType = object : TypeToken<Map<String, CardProgress>>() {}.type
        val progressMap: MutableMap<String, CardProgress> = gson.fromJson(progressJson, progressType) ?: mutableMapOf()
        
        setContent {
            MaterialTheme {
                var selectedLevel by remember { mutableStateOf(initialLevel) }
                val levels = listOf("All", "N5", "N4", "N3", "N2", "N1")
                
                val currentDeck = remember(selectedLevel, allBundledCards) {
                    if (selectedLevel == "All") allBundledCards
                    else allBundledCards.filter { it.level == selectedLevel }
                }
                
                var queue by remember(currentDeck) { mutableStateOf(currentDeck) }
                var currentIndex by remember(queue) { mutableIntStateOf(0) }
                var showAnswer by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("SRS Flashcards") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("Back", fontWeight = FontWeight.Bold)
                                }
                            },
                            actions = {
                                TextButton(onClick = {
                                    val remaining = queue.drop(currentIndex).toMutableList()
                                    remaining.shuffle()
                                    queue = queue.take(currentIndex).toMutableList().apply { addAll(remaining) }
                                    showAnswer = false
                                }) {
                                    Text("Shuffle", fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }
                ) { padding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            
                            // Level Filter Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                levels.forEach { lvl ->
                                    FilterChip(
                                        selected = (selectedLevel == lvl),
                                        onClick = {
                                            selectedLevel = lvl
                                            showAnswer = false
                                        },
                                        label = { Text(lvl, fontSize = 12.sp) }
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            if (queue.isEmpty()) {
                                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Text("No flashcards found for $selectedLevel.", style = MaterialTheme.typography.titleMedium)
                                }
                                return@Column
                            }
                            
                            if (currentIndex >= queue.size) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Session Complete!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("You reviewed all cards in this set.", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(onClick = {
                                        val shuffled = queue.toMutableList().apply { shuffle() }
                                        queue = shuffled
                                        currentIndex = 0
                                        showAnswer = false
                                    }) {
                                        Text("Restart Deck")
                                    }
                                }
                                return@Column
                            }
                            
                            val currentCard = queue[currentIndex]
                            val progressFloat = (currentIndex + 1).toFloat() / queue.size.toFloat()
                            
                            // Progress bar & Card count
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Card ${currentIndex + 1} of ${queue.size}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                AssistChip(
                                    onClick = {},
                                    label = { Text(currentCard.level, fontWeight = FontWeight.Bold) }
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progressFloat },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(modifier = Modifier.height(18.dp))
                            
                            // Card View
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clickable { showAnswer = !showAnswer },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!showAnswer) MaterialTheme.colorScheme.surfaceVariant
                                    else MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp)
                                ) {
                                    if (!showAnswer) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                currentCard.word,
                                                fontSize = 44.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Text(
                                                "Tap card to reveal reading & meaning",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    } else {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                currentCard.word,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                currentCard.reading,
                                                fontSize = 22.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                currentCard.meaning,
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Spacer(modifier = Modifier.height(14.dp))
                                            FilledTonalButton(
                                                onClick = { speakWord(currentCard.word) }
                                            ) {
                                                Text("Pronounce Word")
                                            }
                                            
                                            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))
                                            
                                            Text(
                                                currentCard.exampleSentence,
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                currentCard.exampleTranslation,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(18.dp))
                            
                            // Rating Buttons (SRS)
                            if (showAnswer) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            rateCard(currentCard.id, "again", progressMap, gson, prefs)
                                            showAnswer = false
                                            currentIndex++
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Again", fontSize = 12.sp)
                                    }
                                    
                                    Button(
                                        onClick = {
                                            rateCard(currentCard.id, "hard", progressMap, gson, prefs)
                                            showAnswer = false
                                            currentIndex++
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Hard", fontSize = 12.sp)
                                    }
                                    
                                    Button(
                                        onClick = {
                                            rateCard(currentCard.id, "good", progressMap, gson, prefs)
                                            showAnswer = false
                                            currentIndex++
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Good", fontSize = 12.sp)
                                    }
                                    
                                    Button(
                                        onClick = {
                                            rateCard(currentCard.id, "easy", progressMap, gson, prefs)
                                            showAnswer = false
                                            currentIndex++
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Easy", fontSize = 12.sp)
                                    }
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { showAnswer = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Show Answer")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
    
    private fun rateCard(id: String, rating: String, map: MutableMap<String, CardProgress>, gson: Gson, prefs: android.content.SharedPreferences) {
        val now = Date().time
        val p = map[id] ?: CardProgress(id)
        
        when (rating) {
            "again" -> {
                p.interval = 1
                p.state = "learning"
                p.ease = maxOf(1.3f, p.ease - 0.2f)
            }
            "hard" -> {
                p.interval = maxOf(1, (p.interval * 1.2).toInt())
                p.state = "learning"
                p.ease = maxOf(1.3f, p.ease - 0.15f)
            }
            "good" -> {
                p.interval = if (p.interval < 1440) 1440 else (p.interval * p.ease).toInt()
                p.state = "due"
            }
            "easy" -> {
                p.interval = if (p.interval < 1440) 5760 else (p.interval * p.ease * 1.3).toInt()
                p.state = "due"
                p.ease += 0.15f
            }
        }
        p.nextReviewTime = now + (p.interval * 60 * 1000L)
        map[id] = p
        
        prefs.edit()
            .putString("flashcard_progress", gson.toJson(map))
            .putInt("cards_reviewed", prefs.getInt("cards_reviewed", 0) + 1)
            .apply()
    }
}
