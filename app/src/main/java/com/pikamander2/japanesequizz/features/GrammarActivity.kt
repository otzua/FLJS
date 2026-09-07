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
import androidx.compose.ui.unit.dp
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
        val targetLevel = prefs.getString("jlpt_level", "N5") ?: "N5"
        
        val grammarPoints = try {
            val reader = InputStreamReader(assets.open("grammar.json"))
            val type = object : TypeToken<List<GrammarPoint>>() {}.type
            val all: List<GrammarPoint> = Gson().fromJson(reader, type)
            all.filter { it.level == targetLevel }
        } catch (e: Exception) {
            emptyList()
        }
        
        setContent {
            MaterialTheme {
                var searchQuery by remember { mutableStateOf("") }
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("$targetLevel Grammar Guide") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("Back", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                }
                            }
                        )
                    }
                ) { padding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Search Grammar Patterns") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            val filtered = grammarPoints.filter {
                                it.pattern.contains(searchQuery, ignoreCase = true) ||
                                it.meaning.contains(searchQuery, ignoreCase = true)
                            }
                            
                            LazyColumn {
                                items(filtered) { point ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                                Text(point.pattern, style = MaterialTheme.typography.titleLarge)
                                                Badge { Text(point.category, modifier = Modifier.padding(horizontal = 4.dp)) }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(point.meaning, style = MaterialTheme.typography.bodyLarge)
                                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                            point.examples.forEach { ex ->
                                                Text(ex.furigana, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
                                                Text(ex.translation, style = MaterialTheme.typography.bodySmall)
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
