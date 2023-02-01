package io.github.japskiddin.debuglogger.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.github.japskiddin.debuglogger.adapter.LogAdapter
import io.github.japskiddin.debuglogger.databinding.DebugLoggerBinding
import io.github.japskiddin.debuglogger.manager.LogManager
import io.github.japskiddin.debuglogger.model.LogEvent

class DebugLogger : LinearLayout, DefaultLifecycleObserver {
  private val logHandler = Handler(Looper.getMainLooper())
  private var logAdapter: LogAdapter? = null
  private var binding: DebugLoggerBinding? = null

  constructor(context: Context) : super(context) {
    (context as LifecycleOwner).lifecycle.addObserver(this)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    (context as LifecycleOwner).lifecycle.addObserver(this)
  }

  private val debugLogRunnable: Runnable = object : Runnable {
    override fun run() {
      val logs: List<LogEvent> = ArrayList(LogManager.getInstance()!!.getLogs())
      if (LogManager.getInstance()!!.isEnabled && logs.isNotEmpty()) {
        for (log in logs) {
          logAdapter!!.addItem(log)
        }
        binding!!.rvLogs.scrollToPosition(logAdapter!!.itemCount - 1)
        LogManager.getInstance()!!.clear()
      }
      logHandler.postDelayed(this, HANDLER_DELAY)
    }
  }

  private fun onLogsClear() {
    LogManager.getInstance()!!.clear()
    logAdapter!!.clear()
  }

  private fun onCopyLogs() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied text", logAdapter!!.allText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Text copied", Toast.LENGTH_LONG).show()
  }

  override fun onCreate(owner: LifecycleOwner) {
    binding = DebugLoggerBinding.inflate(LayoutInflater.from(context), this, true)
    logAdapter = LogAdapter()
    binding!!.rvLogs.adapter = logAdapter
    binding!!.btnLogClear.setOnClickListener { onLogsClear() }
    binding!!.btnLogCopy.setOnClickListener { onCopyLogs() }
  }

  override fun onStart(owner: LifecycleOwner) {}
  override fun onStop(owner: LifecycleOwner) {}
  override fun onPause(owner: LifecycleOwner) {
    logHandler.removeCallbacks(debugLogRunnable)
  }

  override fun onResume(owner: LifecycleOwner) {
    logHandler.post(debugLogRunnable)
  }

  override fun onDestroy(owner: LifecycleOwner) {
    logHandler.removeCallbacks(debugLogRunnable)
    binding = null
  }

  companion object {
    private const val HANDLER_DELAY = 2000L
  }
}