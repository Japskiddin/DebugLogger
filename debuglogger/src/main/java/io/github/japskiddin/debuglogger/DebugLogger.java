package io.github.japskiddin.debuglogger;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import io.github.japskiddin.debuglogger.databinding.DebugLoggerBinding;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Convert2Lambda") public class DebugLogger extends LinearLayout implements
    DefaultLifecycleObserver {
  private final static long HANDLER_DELAY = 2000L;
  private final Handler logHandler = new Handler(Looper.getMainLooper());
  private LogAdapter logAdapter;
  private DebugLoggerBinding binding;

  public DebugLogger(Context context) {
    super(context);
    ((LifecycleOwner) context).getLifecycle().addObserver(this);
  }

  public DebugLogger(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    ((LifecycleOwner) context).getLifecycle().addObserver(this);
  }

  private final Runnable debugLogRunnable = new Runnable() {
    @Override public void run() {
      List<LogEvent> logs = new ArrayList<>(LogManager.getInstance().getLogs());
      if (LogManager.getInstance().isEnabled() && logs.size() > 0) {
        for (LogEvent log : logs) {
          logAdapter.addItem(log);
        }
        binding.rvLogs.scrollToPosition(logAdapter.getItemCount() - 1);
        LogManager.getInstance().clear();
      }
      logHandler.postDelayed(this, HANDLER_DELAY);
    }
  };

  private void onLogsClear() {
    LogManager.getInstance().clear();
    logAdapter.clear();
  }

  private void onCopyLogs() {
    ClipboardManager clipboard =
        (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("Copied text", logAdapter.getAllText());
    clipboard.setPrimaryClip(clip);
    Toast.makeText(getContext(), "Text copied", Toast.LENGTH_LONG).show();
  }

  @Override public void onCreate(@NonNull LifecycleOwner owner) {
    binding = DebugLoggerBinding.inflate(LayoutInflater.from(getContext()), this, true);
    logAdapter = new LogAdapter();
    binding.rvLogs.setAdapter(logAdapter);
    binding.btnLogClear.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        onLogsClear();
      }
    });
    binding.btnLogCopy.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        onCopyLogs();
      }
    });
  }

  @Override public void onStart(@NonNull LifecycleOwner owner) {
  }

  @Override public void onStop(@NonNull LifecycleOwner owner) {
  }

  @Override public void onPause(@NonNull LifecycleOwner owner) {
    logHandler.removeCallbacks(debugLogRunnable);
  }

  @Override public void onResume(@NonNull LifecycleOwner owner) {
    logHandler.post(debugLogRunnable);
  }

  @Override public void onDestroy(@NonNull LifecycleOwner owner) {
    logHandler.removeCallbacks(debugLogRunnable);
    binding = null;
  }
}