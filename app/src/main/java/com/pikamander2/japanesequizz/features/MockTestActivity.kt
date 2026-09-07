package com.pikamander2.japanesequizz.features

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager

class MockTestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultLevel = prefs.getString("jlpt_level", "N5") ?: "N5"
        
        fun openLink(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        
        setContent {
            MaterialTheme {
                var selectedLevel by remember { mutableStateOf(defaultLevel) }
                val levels = listOf("N5", "N4", "N3", "N2", "N1")
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("PYQs & Textbooks") },
                            navigationIcon = {
                                TextButton(onClick = { finish() }) {
                                    Text("Back", fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }
                ) { padding ->
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                            
                            // 1. Official JLPT PYQs Section
                            Text("Official JLPT Previous Year Papers", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Direct single-click downloads of official workbooks with complete questions and answer keys.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Level Filter Chips
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                levels.forEach { lvl ->
                                    FilterChip(
                                        selected = (selectedLevel == lvl),
                                        onClick = { selectedLevel = lvl },
                                        label = { Text(lvl) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("$selectedLevel - 2018 Official Exam", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/2018_${selectedLevel}.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Question Paper")
                                        }
                                        OutlinedButton(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/2018_${selectedLevel}_ans.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Answers")
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text("$selectedLevel - 2012 Official Exam", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/2012_${selectedLevel}.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Question Paper")
                                        }
                                        OutlinedButton(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/2012_${selectedLevel}_ans.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Answers")
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { openLink("https://www.jlpt.jp/e/samples/sample12.html") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Official Audio & Listening Section Portal")
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
                            
                            // 2. Genki 3rd Edition Series (Google Drive)
                            Text("Genki (3rd Edition) Library", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Direct single-click downloads from your Google Drive folder. Includes textbooks, workbooks, and answer keys.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://drive.google.com/uc?export=download&id=1Qpvg4MsHQuTx21nAk7wBe8xL214mexRj") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Genki I Textbook (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://drive.google.com/uc?export=download&id=1usI1bSq8OAuFKSQfDed_tD1cmuiaACSB") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Genki I Workbook (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://drive.google.com/uc?export=download&id=1isvdrAakCHwfz8cBuhO8nrHXYYfD1DGT") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Genki II Textbook (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://drive.google.com/uc?export=download&id=1IceUY_s3tWYHrgHktb_mOrUyZP1N8Lsk") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Genki II Workbook (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://drive.google.com/uc?export=download&id=1TVhPCvi5PGivChalty2IF6WLGx_1NBA4") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Genki I & II Answer Key (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            FilledTonalButton(
                                onClick = { openLink("https://drive.google.com/drive/folders/1b30w5jhS6sMCWFUUf4sBW_8q9k53_zZ7") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Open Complete Genki Google Drive Folder")
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
                            
                            // 3. Minna no Nihongo Series
                            Text("Minna no Nihongo Series", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Direct single-click downloads for core textbooks and grammatical explanation notes.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://archive.org/download/minna-no-nihongo-shokyu-i-dai-2-han-honsatsu-kanji-kana_202201/Minna%20no%20Nihongo%20Shokyu%20I%20Dai%202-Han%20Honsatsu%20Kanji-Kana%20%28%20PDFDrive%20%29.pdf") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Minna no Nihongo I Main Book (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://archive.org/download/MinnaNoNihongoIITrans/Minna%20no%20Nihongo%20I-Trans.pdf") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Minna no Nihongo I English Notes (PDF)")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { openLink("https://archive.org/download/MinnaNoNihongoIITrans/Minna%20No%20Nihongo%20II%20-Trans.pdf") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Download Minna no Nihongo II English Notes (PDF)")
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
                            
                            // 4. Interactive Practice
                            Text("Interactive Study Resources", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Online exercises that synchronize with textbook chapters.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            FilledTonalButton(
                                onClick = { openLink("https://sethclydesdale.github.io/genki-study-resources/") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Seth Clydesdale's Genki Study Exercises")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            FilledTonalButton(
                                onClick = { openLink("https://www.renshuu.org/") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Renshuu Interactive Japanese Practice")
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}
