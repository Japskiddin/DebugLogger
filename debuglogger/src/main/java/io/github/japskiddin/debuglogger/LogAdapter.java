package io.github.japskiddin.debuglogger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.japskiddin.debuglogger.databinding.RvDebugLogItemBinding;
import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogHolder> {
  private final List<LogEvent> logs;

  public LogAdapter() {
    logs = new ArrayList<>();
  }

  public void addItem(LogEvent log) {
    logs.add(log);
    notifyItemInserted(logs.size() - 1);
  }

  public void clear() {
    logs.clear();
    notifyDataSetChanged();
  }

  public String getAllText() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < logs.size(); i++) {
      LogEvent log = logs.get(i);
      sb.append(log.toString());
      if (i + 1 < logs.size()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public long getLastTime() {
    if (logs.size() > 0) return logs.get(logs.size() - 1).getTime();
    return 0;
  }

  @NonNull @Override
  public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new LogHolder(
        RvDebugLogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override public void onBindViewHolder(@NonNull LogHolder holder, int position) {
    LogEvent log = logs.get(position);
    holder.bind(log);
  }

  @Override public int getItemCount() {
    return logs.size();
  }

  static class LogHolder extends RecyclerView.ViewHolder {
    private final RvDebugLogItemBinding mBinding;

    public LogHolder(@NonNull RvDebugLogItemBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    void bind(LogEvent log) {
      mBinding.tvLog.setText(log.toString());
    }
  }
}