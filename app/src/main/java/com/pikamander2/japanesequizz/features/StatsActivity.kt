package com.pikamander2.japanesequizz.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager

class StatsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val rawName = prefs.getString("user_name", "User") ?: "User"
        val name = rawName.trim().split(Regex("\\s+")).firstOrNull()?.takeIf { it.isNotBlank() } ?: "User"
        val totalAnswered = prefs.getInt("total_answered", 0)
        val bestStreak = prefs.getInt("best_streak", 0)
        val cardsReviewed = prefs.getInt("cards_reviewed", 0)
        
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("$name's Stats") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("Back", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                }
                            }
                        )
                    }
                ) { padding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Quiz Total Answered", style = MaterialTheme.typography.titleMedium)
                                    Text("$totalAnswered", style = MaterialTheme.typography.displaySmall)
                                }
                            }
                            
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Best Quiz Streak", style = MaterialTheme.typography.titleMedium)
                                    Text("$bestStreak", style = MaterialTheme.typography.displaySmall)
                                }
                            }
                            
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Flashcards Reviewed", style = MaterialTheme.typography.titleMedium)
                                    Text("$cardsReviewed", style = MaterialTheme.typography.displaySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
