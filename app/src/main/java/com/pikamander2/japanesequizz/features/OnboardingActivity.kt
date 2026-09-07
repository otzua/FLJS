package com.pikamander2.japanesequizz.features

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.pikamander2.japanesequizz.MainActivity

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var name by remember { mutableStateOf("") }
                    var level by remember { mutableStateOf("N5") }
                    val levels = listOf("N5", "N4", "N3", "N2", "N1")
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Welcome to FLJS!", style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("What's your name?") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Target JLPT Level:", style = MaterialTheme.typography.titleMedium)
                        
                        levels.forEach { lvl ->
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (level == lvl),
                                    onClick = { level = lvl }
                                )
                                Text(text = lvl)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    val prefs = PreferenceManager.getDefaultSharedPreferences(this@OnboardingActivity)
                                    prefs.edit()
                                        .putString("user_name", name)
                                        .putString("jlpt_level", level)
                                        .apply()
                                        
                                    startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                                    finish()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = name.isNotBlank()
                        ) {
                            Text("Let's Start")
                        }
                    }
                }
            }
        }
    }
}
