package io.github.japskiddin.debuglogger

import java.text.SimpleDateFormat
import java.util.Locale

class LogEvent(private val type: Int, private val tag: String, private val text: String) :
  Comparable<LogEvent> {
  val time: Long = System.currentTimeMillis()

  private val logType: String
    get() = when (type) {
      LOG_INFO -> "INFO"
      LOG_WARN -> "WARN"
      LOG_ERROR -> "ERROR"
      LOG_DEBUG -> "DEBUG"
      else -> "INFO"
    }

  override fun compareTo(other: LogEvent): Int {
    return if (time > other.time) 1 else -1
  }

  override fun toString(): String {
    val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    return sdf.format(time) + " - " + logType + ": " + tag + " / " + text
  }

  companion object {
    const val LOG_INFO = 0
    const val LOG_WARN = 1
    const val LOG_ERROR = 2
    const val LOG_DEBUG = 3
  }
}