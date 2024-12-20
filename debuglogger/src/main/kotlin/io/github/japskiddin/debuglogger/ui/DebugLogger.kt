package io.github.japskiddin.debuglogger.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.github.japskiddin.debuglogger.adapter.LogAdapter
import io.github.japskiddin.debuglogger.databinding.DebugLoggerBinding
import io.github.japskiddin.debuglogger.manager.LogManager

public class DebugLogger
@JvmOverloads
constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = -1,
) : FrameLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {
  private val binding: DebugLoggerBinding = DebugLoggerBinding.inflate(
    LayoutInflater.from(context), this, true
  )
  private val logAdapter: LogAdapter = LogAdapter { scrollListToBottom() }
  private val logHandler = Handler(Looper.getMainLooper())

  init {
    binding.rvLogs.adapter = logAdapter
    binding.btnLogClear.setOnClickListener {
      onLogsClear()
    }
    binding.btnLogCopy.setOnClickListener {
      onCopyLogs()
    }
    (context as LifecycleOwner).lifecycle.addObserver(this)
  }

  private val debugLogRunnable = object : Runnable {
    override fun run() {
      logAdapter.submit(LogManager.getInstance().getLogs())
      logHandler.postDelayed(this, HANDLER_DELAY)
    }
  }

  private fun onLogsClear() {
    LogManager.getInstance().clear()
    logAdapter.clear()
  }

  private fun onCopyLogs() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied text", logAdapter.getAllItemsString())
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context.applicationContext, "Text copied", Toast.LENGTH_LONG).show()
  }

  private fun scrollListToBottom() {
    binding.rvLogs.scrollToPosition(logAdapter.itemCount - 1)
  }

  override fun onPause(owner: LifecycleOwner) {
    logHandler.removeCallbacks(debugLogRunnable)
  }

  override fun onResume(owner: LifecycleOwner) {
    logHandler.post(debugLogRunnable)
  }

  override fun onDestroy(owner: LifecycleOwner) {
    logHandler.removeCallbacks(debugLogRunnable)
  }

  private companion object {
    private const val HANDLER_DELAY = 2000L
  }
}
