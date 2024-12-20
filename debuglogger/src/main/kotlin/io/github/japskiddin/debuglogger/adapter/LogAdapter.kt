package io.github.japskiddin.debuglogger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.japskiddin.debuglogger.adapter.LogAdapter.LogHolder
import io.github.japskiddin.debuglogger.databinding.ItemDebugLogBinding
import io.github.japskiddin.debuglogger.model.LogEvent

internal typealias ListChangeListener = () -> Unit

internal class LogAdapter(
  private val listChangeListener: ListChangeListener
) : ListAdapter<LogEvent, LogHolder>(DIFF_CALLBACK) {
  internal fun submit(logs: List<LogEvent>) {
    val list = ArrayList(logs)
    submitList(list)
  }

  internal fun clear() {
    submitList(ArrayList())
  }

  internal fun getAllItemsString(): String = StringBuilder().apply {
    currentList.forEachIndexed { index, logEvent ->
      append(logEvent.toString())
      if (index + 1 < currentList.size) {
        append("\n")
      }
    }
  }.toString()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = ItemDebugLogBinding.inflate(inflater, parent, false)
    return LogHolder(binding)
  }

  override fun onBindViewHolder(holder: LogHolder, position: Int) {
    val log = currentList[position]
    holder.bind(log)
  }

  override fun getItemCount(): Int = currentList.size

  override fun onCurrentListChanged(
    previousList: MutableList<LogEvent>,
    currentList: MutableList<LogEvent>
  ) {
    super.onCurrentListChanged(previousList, currentList)
    listChangeListener()
  }

  internal class LogHolder(
    private val binding: ItemDebugLogBinding
  ) : ViewHolder(binding.root) {
    fun bind(log: LogEvent) {
      binding.tvLog.text = log.toString()
    }
  }

  private companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LogEvent>() {
      override fun areItemsTheSame(oldItem: LogEvent, newItem: LogEvent): Boolean =
        oldItem.areItemsTheSame(newItem)

      override fun areContentsTheSame(oldItem: LogEvent, newItem: LogEvent): Boolean =
        oldItem.areContentsTheSame(newItem)
    }
  }
}
