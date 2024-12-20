package io.github.japskiddin.debuglogger.manager

import android.os.Build
import io.github.japskiddin.debuglogger.model.Level
import io.github.japskiddin.debuglogger.model.LogEvent
import java.util.concurrent.atomic.AtomicBoolean

public class LogManager private constructor() {
  public var enabled: AtomicBoolean = AtomicBoolean(false)
  private val logs: MutableList<LogEvent> = mutableListOf()

  internal fun getLogs(): List<LogEvent> = logs

  internal fun clear() {
    synchronized(logs) {
      logs.clear()
    }
  }

  public fun logInfo(tag: String, event: String) {
    addToLog(Level.INFO, tag, event)
  }

  public fun logError(tag: String, event: String) {
    addToLog(Level.ERROR, tag, event)
  }

  public fun logDebug(tag: String, event: String) {
    addToLog(Level.DEBUG, tag, event)
  }

  public fun logWarn(tag: String, event: String) {
    addToLog(Level.WARN, tag, event)
  }

  private fun addToLog(type: Level, tag: String, event: String) {
    val log = LogEvent(type, tag, event)
    synchronized(logs) {
      logs.add(log)
      if (logs.size > LIST_LIMIT) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
          logs.removeFirst()
        } else {
          logs.removeAt(0)
        }
      }
    }
  }

  public companion object {
    private const val LIST_LIMIT: Int = 30

    @Volatile
    private var instance: LogManager? = null

    @JvmStatic
    public fun getInstance(): LogManager {
      if (instance == null) {
        synchronized(this) {
          if (instance == null) {
            instance = LogManager()
          }
        }
      }
      return instance!!
    }
  }
}
