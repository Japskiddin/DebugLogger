package io.github.japskiddin.debuglogger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.japskiddin.debuglogger.adapter.LogAdapter.LogHolder
import io.github.japskiddin.debuglogger.databinding.RvDebugLogItemBinding
import io.github.japskiddin.debuglogger.model.LogEvent

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
        notifyItemRangeRemoved(0, itemCount)
        logs.clear()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val binding =
            RvDebugLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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