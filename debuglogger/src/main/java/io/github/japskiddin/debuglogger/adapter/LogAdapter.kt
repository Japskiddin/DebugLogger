package io.github.japskiddin.debuglogger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.japskiddin.debuglogger.adapter.LogAdapter.LogHolder
import io.github.japskiddin.debuglogger.databinding.RvDebugLogItemBinding
import io.github.japskiddin.debuglogger.model.LogEvent

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LogEvent>() {
    override fun areItemsTheSame(oldItem: LogEvent, newItem: LogEvent): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: LogEvent, newItem: LogEvent): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}

typealias ListChangeListener = () -> Unit

class LogAdapter(private val listChangeListener: ListChangeListener) :
    ListAdapter<LogEvent, LogHolder>(DIFF_CALLBACK) {
    fun submit(logs: List<LogEvent>) {
        val list = ArrayList(logs)
        submitList(list)
    }

    fun clear() {
        submitList(ArrayList())
    }

    fun getAllItemsString(): String {
        val sb = StringBuilder()
        for (i in currentList.indices) {
            val log = currentList[i]
            sb.append(log.toString())
            if (i + 1 < currentList.size) {
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
        val log = currentList[position]
        holder.bind(log)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCurrentListChanged(
        previousList: MutableList<LogEvent>,
        currentList: MutableList<LogEvent>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        listChangeListener()
    }

    class LogHolder(private val binding: RvDebugLogItemBinding) : ViewHolder(binding.root) {
        fun bind(log: LogEvent) {
            binding.tvLog.text = log.toString()
        }
    }
}