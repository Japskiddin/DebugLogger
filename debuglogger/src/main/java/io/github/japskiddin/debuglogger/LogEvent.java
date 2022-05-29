package io.github.japskiddin.debuglogger;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogEvent implements Comparable<LogEvent> {
  private final long time;
  private final String tag;
  private final String text;

  public LogEvent(@NonNull String tag, String text) {
    this.time = System.currentTimeMillis();
    this.tag = tag;
    this.text = text;
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

  @Override
  public int compareTo(LogEvent logEvent) {
    return this.time > logEvent.time ? 1 : -1;
  }

  @NonNull @Override public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    return tag + " - " + sdf.format(time) + " - " + text;
  }
}