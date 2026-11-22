package com.example.studentsfeesstructure.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.studentsfeesstructure.R
import com.example.studentsfeesstructure.data.AppDatabase

class FeeReminderWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val owing = AppDatabase.get(applicationContext).studentDao().countOwing()
        if (owing > 0) showNotification(owing)
        return Result.success()
    }

    private fun showNotification(count: Int) {
        val allowed = Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (!allowed) return

        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Fee reminders", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        val n = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Fee balances outstanding")
            .setContentText("$count student(s) still owe fees.")
            .setAutoCancel(true).build()
        manager.notify(1, n)
    }
    companion object { const val CHANNEL_ID = "fee_reminders" }
}