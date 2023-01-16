package io.github.japskiddin.debuglogger.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import io.github.japskiddin.debuglogger.LogManager.Companion.getInstance
import io.github.japskiddin.debuglogger.LogManager.Companion.init
import io.github.japskiddin.debuglogger.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  // TODO: 29.05.2022 поучиться написанию тестов
  private var binding: ActivityMainBinding? = null
  private val testMessageHandler = Handler(Looper.getMainLooper())
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    init()
    getInstance()!!.isEnabled = true
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding!!.root)
    testMessageHandler.post(testMessageRunnable)
  }

  override fun onStart() {
    super.onStart()
    getInstance()!!.logDebug("Activity", "onStart")
  }

  override fun onPause() {
    getInstance()!!.logDebug("Activity", "onPause")
    super.onPause()
  }

  override fun onResume() {
    super.onResume()
    getInstance()!!.logDebug("Activity", "onResume")
  }

  override fun onDestroy() {
    testMessageHandler.removeCallbacks(testMessageRunnable)
    binding = null
    super.onDestroy()
  }

  private val testMessageRunnable: Runnable = object : Runnable {
    override fun run() {
      getInstance()!!.logInfo("Test", "New message")
      testMessageHandler.postDelayed(this, 5000)
    }
  }
}