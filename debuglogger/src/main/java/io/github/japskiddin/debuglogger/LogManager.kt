package io.github.japskiddin.debuglogger

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
    addToLog(LogEvent.LOG_INFO, tag, event)
  }

  fun logError(tag: String, event: String) {
    addToLog(LogEvent.LOG_ERROR, tag, event)
  }

  fun logDebug(tag: String, event: String) {
    addToLog(LogEvent.LOG_DEBUG, tag, event)
  }

  fun logWarn(tag: String, event: String) {
    addToLog(LogEvent.LOG_WARN, tag, event)
  }

  private fun addToLog(type: Int, tag: String, event: String) {
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