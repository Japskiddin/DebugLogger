package io.github.japskiddin.debuglogger;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogEvent implements Comparable<LogEvent> {
  public static final int LOG_INFO = 0;
  public static final int LOG_WARN = 1;
  public static final int LOG_ERROR = 2;
  public static final int LOG_DEBUG = 3;
  private final long time;
  private final String tag;
  private final String text;
  private final int type;

  public LogEvent(int type, @NonNull String tag, String text) {
    this.time = System.currentTimeMillis();
    this.tag = tag;
    this.text = text;
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public String getTag() {
    return tag;
  }

  public long getTime() {
    return time;
  }

  public String getText() {
    return text;
  }

  private String getLogType() {
    switch (type) {
      case LOG_INFO:
      default:
        return "INFO";
      case LOG_WARN:
        return "WARN";
      case LOG_ERROR:
        return "ERROR";
      case LOG_DEBUG:
        return "DEBUG";
    }
  }

  @Override
  public int compareTo(LogEvent logEvent) {
    return this.time > logEvent.time ? 1 : -1;
  }

  @NonNull @Override public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    return sdf.format(time) + " - " + getLogType() + ": " + tag + " / " + text;
  }
}