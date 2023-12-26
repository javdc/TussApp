package com.javdc.tussapp.presentation.ui.stops

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.common.util.safeLet
import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowLineStopBinding

class LineStopAdapter(private val listener: (Int, View) -> Unit) : ListAdapter<LineStopBo, LineStopAdapter.ViewHolder>(lineStopDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowLineStopBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
        holder.itemView.setOnClickListener { listener(item.code, holder.itemView) }
    }

    inner class ViewHolder(private val binding: RowLineStopBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LineStopBo, position: Int) {
            binding.apply {
                root.transitionName = root.context.getString(R.string.row_stop_transition_name, item.code)
                lineStopTopLineView.isVisible = position != 0
                lineStopBottomLineView.isVisible = position != itemCount - 1
                lineStopDescriptionTextView.text = item.description
                lineStopNumberTextView.text = root.context.getString(R.string.stop_format, item.code)
                lineStopScheduleTextView.text = safeLet(item.startTime, item.endTime) { startTime, endTime ->
                    "$startTime - $endTime"
                }
            }
        }

    }

    private companion object {
        private val lineStopDiffCallback = object : DiffUtil.ItemCallback<LineStopBo>() {
            override fun areItemsTheSame(oldItem: LineStopBo, newItem: LineStopBo): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: LineStopBo, newItem: LineStopBo): Boolean =
                oldItem == newItem
        }
    }
}