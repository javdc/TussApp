package com.javdc.tussapp.presentation.ui.lines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowLineBinding
import com.javdc.tussapp.presentation.model.LineVo

class LineAdapter(private val listener: (View, LineVo) -> Unit) : ListAdapter<LineVo, LineAdapter.ViewHolder>(lineDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowLineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(holder.itemView, item) }
    }

    inner class ViewHolder(private val binding: RowLineBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LineVo) {
            binding.apply {
                root.transitionName = root.context.getString(R.string.row_line_transition_name, item.id)
                lineCodeTextView.text = item.label
                lineCodeTextView.background.mutate().setTint(item.color)
                lineDescriptionTextView.text = item.description
                lineScheduleTextView.text = item.schedule
            }
        }

    }

    private companion object {
        private val lineDiffCallback = object : DiffUtil.ItemCallback<LineVo>() {
            override fun areItemsTheSame(oldItem: LineVo, newItem: LineVo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: LineVo, newItem: LineVo): Boolean =
                oldItem == newItem
        }
    }
}