package io.github.japskiddin.debuglogger;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogEvent implements Comparable<LogEvent> {
  private long time = System.currentTimeMillis();
  private String tag;
  private String text;

  public LogEvent(@NonNull String tag, String text) {
    this.tag = tag;
    this.text = text;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(@NonNull String tag) {
    this.tag = tag;
  }

  public long getTime() {
    return time;
  }

  public String getTimeString() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    return sdf.format(time);
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public int compareTo(LogEvent logEvent) {
    return this.time > logEvent.time ? 1 : -1;
  }
}