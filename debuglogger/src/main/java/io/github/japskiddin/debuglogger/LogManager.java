package io.github.japskiddin.debuglogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogManager {
  private static LogManager instance;
  private boolean enabled = false;
  private final List<LogEvent> logs = new ArrayList<>();

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
    Collections.sort(logs);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}