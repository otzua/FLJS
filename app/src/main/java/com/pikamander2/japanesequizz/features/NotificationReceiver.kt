package com.pikamander2.japanesequizz.features

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.pikamander2.japanesequizz.MainActivity
import android.os.Build

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val rawName = prefs.getString("user_name", "Bhai") ?: "Bhai"
        val name = rawName.trim().split(Regex("\\s+")).firstOrNull()?.takeIf { it.isNotBlank() } ?: "Bhai"
        val level = prefs.getString("jlpt_level", "N5") ?: "N5"
        
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("study_reminders", "Study Reminders", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }
        
        val i = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
        
        val aggressiveAlerts = listOf(
            "Padh le $name nalayak! Baap ka paisa barbaad mat kar, $level kaun padhega?",
            "Abe aalsi ke dher $name! Phone scroll band kar aur chup chap padh le!",
            "Kitna timepass karega $name? Fail hone ka pura plan bana liya hai kya?",
            "Sharam kar thodi $name, JLPT me ande leke aayega kya? Flashcard khol abhi!",
            "Padh le $name, varna baad me roye ga exam hall me!",
            "Abe ghonchu $name, 4 ghante se ek word nahi padha tune! Jaldi revision kar!",
            "Agar abhi nahi padha $name toh $level bhul ja! Fatafat practice start kar!",
            "Bhai $name, tere se bada aalsi insaan nahi dekha maine! Khol app jaldi!",
            "Exam sar par hai $name, reel dekhna band kar aur Japanese padh!"
        )
        val alertMsg = aggressiveAlerts.random()
        
        val builder = NotificationCompat.Builder(context, "study_reminders")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Padhai Kar Le!")
            .setContentText(alertMsg)
            .setStyle(NotificationCompat.BigTextStyle().bigText(alertMsg))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
            
        nm.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
