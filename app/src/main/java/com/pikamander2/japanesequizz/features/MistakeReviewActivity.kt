package com.pikamander2.japanesequizz.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MistakeReviewActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Mistakes Review") },
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
                            Text("Review items you got wrong.")
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Q: 食べる (to read)", color = MaterialTheme.colorScheme.error)
                                    Text("Correct Answer: to eat", style = MaterialTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Explanation: 食べる (たべる) means 'to eat'. 読む (よむ) means 'to read'.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
