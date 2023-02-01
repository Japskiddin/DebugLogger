package io.github.japskiddin.debuglogger.manager

import io.github.japskiddin.debuglogger.model.Level
import io.github.japskiddin.debuglogger.model.LogEvent

class LogManager private constructor() {
  var isEnabled = false
  private val logs: MutableList<LogEvent>

  init {
    logs = ArrayList()
  }

  fun getLogs(): List<LogEvent> {
    return logs
  }

  fun clear() {
    synchronized(logs) { logs.clear() }
  }

  fun logInfo(tag: String, event: String) {
    addToLog(Level.INFO, tag, event)
  }

  fun logError(tag: String, event: String) {
    addToLog(Level.ERROR, tag, event)
  }

  fun logDebug(tag: String, event: String) {
    addToLog(Level.DEBUG, tag, event)
  }

  fun logWarn(tag: String, event: String) {
    addToLog(Level.WARN, tag, event)
  }

  private fun addToLog(type: Level, tag: String, event: String) {
    if (!isEnabled) return
    synchronized(logs) {
      val log = LogEvent(type, tag, event)
      logs.add(log)
    }
  }

  companion object {
    private var instance: LogManager? = null
    @JvmStatic fun getInstance(): LogManager? {
      var localInstance = instance
      if (localInstance == null) {
        synchronized(LogManager::class.java) {
          localInstance = instance
          if (localInstance == null) {
            localInstance = LogManager()
            instance = localInstance
          }
        }
      }
      return localInstance
    }

    @JvmStatic fun init() {
      if (instance == null) {
        instance = LogManager()
      }
    }
  }
}