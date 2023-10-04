package io.github.japskiddin.debuglogger.model

import java.text.SimpleDateFormat
import java.util.Locale

data class LogEvent(private val type: Level, private val tag: String, private val text: String) :
    Comparable<LogEvent> {
    private val time: Long = System.currentTimeMillis()

    override fun compareTo(other: LogEvent): Int {
        return time.compareTo(other.time)
    }

    override fun toString(): String {
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        return "${sdf.format(time)} - $type : $tag / $text"
    }
}