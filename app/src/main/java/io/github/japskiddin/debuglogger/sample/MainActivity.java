package io.github.japskiddin.debuglogger.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import io.github.japskiddin.debuglogger.LogManager;
import io.github.japskiddin.debuglogger.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
  // TODO: 29.05.2022 поучиться написанию тестов

  private ActivityMainBinding binding;
  private final Handler testMessageHandler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LogManager.init();
    LogManager.getInstance().setEnabled(true);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    testMessageHandler.post(testMessageRunnable);
  }

  @Override protected void onStart() {
    super.onStart();
    LogManager.getInstance().logDebug("Activity", "onStart");
  }

  @Override protected void onPause() {
    LogManager.getInstance().logDebug("Activity", "onPause");
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    LogManager.getInstance().logDebug("Activity", "onResume");
  }

  @Override protected void onDestroy() {
    testMessageHandler.removeCallbacks(testMessageRunnable);
    binding = null;
    super.onDestroy();
  }

  private final Runnable testMessageRunnable = new Runnable() {
    @Override public void run() {
      LogManager.getInstance().logInfo("Test", "New message");
      testMessageHandler.postDelayed(this, 5000);
    }
  };
}