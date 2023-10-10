package io.github.japskiddin.debuglogger.manager

import io.github.japskiddin.debuglogger.model.Level
import io.github.japskiddin.debuglogger.model.LogEvent

class LogManager private constructor() {
    var enabled = false
    private val logs: MutableList<LogEvent> = mutableListOf()

    fun getLogs(): MutableList<LogEvent> {
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
        if (!enabled) return
        synchronized(logs) {
            val log = LogEvent(type, tag, event)
            logs.add(log)
        }
    }

    companion object {
        @Volatile
        private lateinit var instance: LogManager

        @JvmStatic
        fun getInstance(): LogManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = LogManager()
                }
                return instance
            }
        }
    }
}