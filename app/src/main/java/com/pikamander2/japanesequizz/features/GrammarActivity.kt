package com.pikamander2.japanesequizz.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class Example(val japanese: String, val furigana: String, val translation: String)
data class GrammarPoint(val level: String, val pattern: String, val meaning: String, val category: String, val examples: List<Example>)

class GrammarActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val initialLevel = prefs.getString("jlpt_level", "N5") ?: "N5"
        
        val allGrammarPoints: List<GrammarPoint> = try {
            val reader = InputStreamReader(assets.open("grammar.json"))
            val type = object : TypeToken<List<GrammarPoint>>() {}.type
            Gson().fromJson(reader, type)
        } catch (e: Exception) {
            emptyList()
        }
        
        setContent {
            MaterialTheme {
                var selectedLevel by remember { mutableStateOf(initialLevel) }
                val levels = listOf("All", "N5", "N4", "N3", "N2", "N1")
                var searchQuery by remember { mutableStateOf("") }
                
                val currentPoints = remember(selectedLevel, allGrammarPoints) {
                    if (selectedLevel == "All") allGrammarPoints
                    else allGrammarPoints.filter { it.level == selectedLevel }
                }
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Grammar Reference") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("Back", fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }
                ) { padding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Level Filter Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                levels.forEach { lvl ->
                                    FilterChip(
                                        selected = (selectedLevel == lvl),
                                        onClick = { selectedLevel = lvl },
                                        label = { Text(lvl, fontSize = 12.sp) }
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Search by pattern or English meaning") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            val filtered = currentPoints.filter {
                                it.pattern.contains(searchQuery, ignoreCase = true) ||
                                it.meaning.contains(searchQuery, ignoreCase = true) ||
                                it.category.contains(searchQuery, ignoreCase = true)
                            }
                            
                            Text(
                                "${filtered.size} grammar points available",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(filtered) { point ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    point.pattern,
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                AssistChip(
                                                    onClick = {},
                                                    label = { Text("${point.level} • ${point.category}") }
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                point.meaning,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                            
                                            Text(
                                                "Examples:",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            point.examples.forEach { ex ->
                                                Text(
                                                    ex.japanese,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    ex.furigana,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    ex.translation,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
