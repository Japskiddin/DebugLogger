package io.github.japskiddin.debuglogger;

import java.util.ArrayList;
import java.util.List;

public class LogManager {
  private static LogManager instance;
  private boolean enabled;
  private final List<LogEvent> logs;

  public static LogManager getInstance() {
    LogManager localInstance = instance;
    if (localInstance == null) {
      synchronized (LogManager.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new LogManager();
        }
      }
    }
    return localInstance;
  }

  public static void init() {
    if (instance == null) {
      instance = new LogManager();
    }
  }

  private LogManager() {
    logs = new ArrayList<>();
    enabled = false;
  }

  public List<LogEvent> getLogs() {
    return logs;
  }

  public void clear() {
    logs.clear();
  }

  public void addToLog(String tag, String event) {
    if (!enabled) return;
    LogEvent log = new LogEvent(tag, event);
    logs.add(log);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}