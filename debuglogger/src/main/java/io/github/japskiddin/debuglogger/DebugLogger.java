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
import androidx.annotation.Nullable;
import io.github.japskiddin.debuglogger.databinding.DebugLoggerBinding;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Convert2Lambda") public class DebugLogger extends LinearLayout {
  private final static long HANDLER_DELAY = 2000L;
  private final Handler logHandler = new Handler(Looper.getMainLooper());
  private LogAdapter logAdapter;
  private DebugLoggerBinding binding;

  public DebugLogger(Context context) {
    super(context);
    init(context);
  }

  public DebugLogger(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    binding = DebugLoggerBinding.inflate(LayoutInflater.from(context), this, true);
    logAdapter = new LogAdapter();
    binding.rvLogs.setAdapter(logAdapter);
    binding.btnLogClear.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        LogManager.getInstance().clear();
        logAdapter.clear();
      }
    });
    binding.btnLogCopy.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        ClipboardManager clipboard =
            (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied text", logAdapter.getAllText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Text copied", Toast.LENGTH_LONG).show();
      }
    });
  }

  public void pause() {
    logHandler.removeCallbacks(debugLogRunnable);
  }

  public void resume() {
    logHandler.postDelayed(debugLogRunnable, HANDLER_DELAY);
  }

  public void destroy() {
    pause();
    binding = null;
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
}