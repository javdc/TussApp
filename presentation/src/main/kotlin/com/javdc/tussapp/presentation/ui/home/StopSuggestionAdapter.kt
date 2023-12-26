package com.javdc.tussapp.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowStopSuggestionBinding

class StopSuggestionAdapter(private val listener: (Int, View) -> Unit) : ListAdapter<StopBo, StopSuggestionAdapter.ViewHolder>(stopSuggestionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowStopSuggestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item.code, holder.itemView) }
    }

    public override fun getItem(position: Int): StopBo = super.getItem(position)

    inner class ViewHolder(private val binding: RowStopSuggestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StopBo) {
            binding.apply {
                root.transitionName = root.context.getString(R.string.row_stop_suggestion_transition_name, item.code)
                stopSuggestionDescriptionTextView.text = item.description
                stopSuggestionCodeTextView.text = item.code.toString()
                stopSuggestionLinesTextView.text = binding.root.context.getString(R.string.line_format, item.lines.map { it.label }.joinToString())
            }
        }

    }

    private companion object {
        private val stopSuggestionDiffCallback = object : DiffUtil.ItemCallback<StopBo>() {
            override fun areItemsTheSame(oldItem: StopBo, newItem: StopBo): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: StopBo, newItem: StopBo): Boolean =
                oldItem == newItem
        }
    }
}