package io.github.japskiddin.debuglogger.model

import java.text.SimpleDateFormat
import java.util.Locale

public data class LogEvent(
  private val type: Level,
  private val tag: String,
  private val text: String
) : Comparable<LogEvent> {
  private val time: Long = System.currentTimeMillis()

  internal fun areItemsTheSame(other: LogEvent): Boolean = time == other.time

  internal fun areContentsTheSame(other: LogEvent): Boolean =
    type == other.type
      && tag == other.tag
      && text == other.text

  override fun compareTo(other: LogEvent): Int = time.compareTo(other.time)

  override fun toString(): String {
    val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    return "${sdf.format(time)} - $type: $tag / $text"
  }
}
