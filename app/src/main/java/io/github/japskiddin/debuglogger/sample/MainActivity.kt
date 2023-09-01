package io.github.japskiddin.debuglogger.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import io.github.japskiddin.debuglogger.manager.LogManager
import io.github.japskiddin.debuglogger.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // TODO: 29.05.2022 поучиться написанию тестов

    private lateinit var binding: ActivityMainBinding
    private val testMessageHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogManager.getInstance().setEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        testMessageHandler.post(testMessageRunnable)
    }

    override fun onStart() {
        super.onStart()
        LogManager.getInstance().logDebug("Activity", "onStart")
    }

    override fun onPause() {
        LogManager.getInstance().logDebug("Activity", "onPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        LogManager.getInstance().logDebug("Activity", "onResume")
    }

    override fun onDestroy() {
        testMessageHandler.removeCallbacks(testMessageRunnable)
        super.onDestroy()
    }

    private val testMessageRunnable: Runnable = object : Runnable {
        override fun run() {
            LogManager.getInstance().logInfo("Test", "New message")
            testMessageHandler.postDelayed(this, 5000)
        }
    }
}