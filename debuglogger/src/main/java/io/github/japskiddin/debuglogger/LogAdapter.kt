package io.github.japskiddin.debuglogger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.japskiddin.debuglogger.LogAdapter.LogHolder
import io.github.japskiddin.debuglogger.databinding.RvDebugLogItemBinding

class LogAdapter : Adapter<LogHolder>() {
  private val logs: MutableList<LogEvent>

  init {
    logs = ArrayList()
  }

  fun addItem(log: LogEvent) {
    logs.add(log)
    notifyItemInserted(logs.size - 1)
  }

  fun clear() {
    logs.clear()
    notifyDataSetChanged()
  }

  val allText: String
    get() {
      val sb = StringBuilder()
      for (i in logs.indices) {
        val log = logs[i]
        sb.append(log.toString())
        if (i + 1 < logs.size) {
          sb.append("\n")
        }
      }
      return sb.toString()
    }
  
  val lastTime: Long
    get() = if (logs.size > 0) logs[logs.size - 1].time else 0

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
    val binding = RvDebugLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return LogHolder(binding)
  }

  override fun onBindViewHolder(holder: LogHolder, position: Int) {
    val log = logs[position]
    holder.bind(log)
  }

  override fun getItemCount(): Int {
    return logs.size
  }

  class LogHolder(private val mBinding: RvDebugLogItemBinding) : ViewHolder(mBinding.root) {
    fun bind(log: LogEvent) {
      mBinding.tvLog.text = log.toString()
    }
  }
}