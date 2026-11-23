package com.example.studentsfeesstructure.ui

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.studentsfeesstructure.R
import com.example.studentsfeesstructure.databinding.ActivityMainBinding
import com.example.studentsfeesstructure.work.FeeReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    // 1. Add the permission launcher
    private val askNotifications =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw behind the status bar; light status bar icons on the blue top bar
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = host.navController

        // The two bottom tabs are top-level screens, so they show no back arrow
        val tabs = setOf(R.id.studentListFragment, R.id.summaryFragment)
        appBarConfig = AppBarConfiguration(tabs)
        setupActionBarWithNavController(navController, appBarConfig)
        binding.bottomNav.setupWithNavController(navController)

        // Show the bottom bar only on the tab screens, not on detail or form
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.isVisible = destination.id in tabs
            ViewCompat.requestApplyInsets(binding.root)
        }

        // Keep content clear of the status bar, navigation bar and keyboard
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            binding.appBar.updatePadding(left = bars.left, top = bars.top, right = bars.right)
            binding.navHost.updatePadding(
                left = bars.left, right = bars.right,
                bottom = if (binding.bottomNav.isVisible) ime.bottom else maxOf(bars.bottom, ime.bottom)
            )
            insets
        }

        // 2. Schedule the background work
        val request = PeriodicWorkRequestBuilder<FeeReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "fee_reminder", ExistingPeriodicWorkPolicy.KEEP, request
        )

        // 3. Ask for notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= 33) {
            askNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onSupportNavigateUp() =
        navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
}
