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
import androidx.compose.ui.unit.sp
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
                                "Single-click downloads for official workbooks and past test compilations. (Note: The Japan Foundation officially publishes select full workbooks to maintain exam integrity).",
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
                            
                            // 2018 Official Exam
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("$selectedLevel - 2018 Official Practice Exam (Vol. 2)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Text("Complete full-length exam questions from actual recent JLPT administrations.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(10.dp))
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
                                            Text("Answer Key")
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // 2012 Official Exam
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("$selectedLevel - 2012 Official Practice Exam (Vol. 1)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Text("Official foundational question paper set covering all sections.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(10.dp))
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
                                            Text("Answer Key")
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Official Standard Specimen Paper
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("$selectedLevel - Standard Specimen Exam Set", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Text("Official benchmark question paper and scoring sheet.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/${selectedLevel}-mondai.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Specimen Paper")
                                        }
                                        OutlinedButton(
                                            onClick = { openLink("https://www.jlpt.jp/e/samples/pdf/${selectedLevel}-kaitou.pdf") },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Scoring Sheet")
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // More Multi-Year PYQs & Mock Hubs
                            Text("More Practice Exams & Audio Archives", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            FilledTonalButton(
                                onClick = { openLink("https://jlptsensei.com/downloads/jlpt-practice-tests/") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("JLPT Sensei Multi-Year Practice Test Vault")
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            FilledTonalButton(
                                onClick = { openLink("https://www.jlpt.jp/e/samples/sample12.html") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Official Audio Listening Files & Scripts")
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                            
                            // 2. Genki 3rd Edition Series (Google Drive)
                            Text("Genki (3rd Edition) Library", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Essential Japanese learning books with clear labels for each book's purpose.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            // Genki I Textbook
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Genki I - Main Lesson Book (Textbook)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("For studying: Contains all grammar rules, dialogue lessons, vocabulary lists, and culture notes.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { openLink("https://drive.google.com/uc?export=download&id=1Qpvg4MsHQuTx21nAk7wBe8xL214mexRj") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Genki I Study Book (PDF)")
                                    }
                                }
                            }
                            
                            // Genki I Workbook
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Genki I - Practice Exercises Book (Workbook)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("For practicing: Practice writing, fill-in-the-blank drills, and homework questions matching each chapter.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { openLink("https://drive.google.com/uc?export=download&id=1usI1bSq8OAuFKSQfDed_tD1cmuiaACSB") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Genki I Practice Exercises (PDF)")
                                    }
                                }
                            }
                            
                            // Genki II Textbook
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Genki II - Main Lesson Book (Textbook)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("For studying: Advanced beginner lessons covering JLPT N4 level grammar, compound sentences, and kanji.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { openLink("https://drive.google.com/uc?export=download&id=1isvdrAakCHwfz8cBuhO8nrHXYYfD1DGT") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Genki II Study Book (PDF)")
                                    }
                                }
                            }
                            
                            // Genki II Workbook
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Genki II - Practice Exercises Book (Workbook)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("For practicing: Writing exercises, grammar drills, and listening worksheets for Genki II.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { openLink("https://drive.google.com/uc?export=download&id=1IceUY_s3tWYHrgHktb_mOrUyZP1N8Lsk") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Genki II Practice Exercises (PDF)")
                                    }
                                }
                            }
                            
                            // Answer Key
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Genki I & II - Solutions Guide (Answer Key)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("For checking work: Full solutions and correct answers to every single question in both textbooks and workbooks.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { openLink("https://drive.google.com/uc?export=download&id=1TVhPCvi5PGivChalty2IF6WLGx_1NBA4") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Complete Solutions Key (PDF)")
                                    }
                                }
                            }
                            
                            FilledTonalButton(
                                onClick = { openLink("https://drive.google.com/drive/folders/1b30w5jhS6sMCWFUUf4sBW_8q9k53_zZ7") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Open Full Genki Google Drive (Audio & Extra Files)")
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                            
                            // 3. Minna no Nihongo Series
                            Text("Minna no Nihongo Series", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Popular alternative textbook series with dedicated English grammar translation manuals.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            // Minna 1 Main
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Minna no Nihongo I - Japanese Text (Main Book)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("Immersion text: All Japanese dialogues, kanji, reading passages, and pattern practices.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { openLink("https://archive.org/download/minna-no-nihongo-shokyu-i-dai-2-han-honsatsu-kanji-kana_202201/Minna%20no%20Nihongo%20Shokyu%20I%20Dai%202-Han%20Honsatsu%20Kanji-Kana%20%28%20PDFDrive%20%29.pdf") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download Main Japanese Textbook (PDF)")
                                    }
                                }
                            }
                            
                            // Minna 1 Notes
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Minna no Nihongo I - English Translation & Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("Essential companion: English explanations of all grammar points and vocabulary definitions.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { openLink("https://archive.org/download/MinnaNoNihongoIITrans/Minna%20no%20Nihongo%20I-Trans.pdf") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download English Grammar Guide I (PDF)")
                                    }
                                }
                            }
                            
                            // Minna 2 Notes
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("Minna no Nihongo II - English Translation & Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("Essential companion: English explanations for second half of the beginner curriculum (N4).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { openLink("https://archive.org/download/MinnaNoNihongoIITrans/Minna%20No%20Nihongo%20II%20-Trans.pdf") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Download English Grammar Guide II (PDF)")
                                    }
                                }
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                            
                            // 4. Interactive Practice
                            Text("Interactive Study Resources", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Web-based interactive drills and exercises aligned with textbook lessons.",
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
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}
