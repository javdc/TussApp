package com.javdc.tussapp.presentation.ui.estimates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowStopEstimateBinding
import com.javdc.tussapp.presentation.model.StopEstimateVo
import com.javdc.tussapp.presentation.util.getFormattedTimeLeftString

class StopEstimateAdapter: ListAdapter<StopEstimateVo, StopEstimateAdapter.ViewHolder>(stopEstimateDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowStopEstimateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: RowStopEstimateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StopEstimateVo) {
            binding.apply {
                stopEstimateLineCodeTextView.text = item.lineLabel ?: root.context.getString(R.string.unknown)
                stopEstimateLineCodeTextView.background.mutate().setTint(item.lineColor)
                stopEstimateDestinationTextView.text = item.destination
                stopEstimateDestinationTextView.isSelected = true
                stopEstimateTimeLeftTextView.text = getFormattedTimeLeftString(root.context, item.seconds)
            }
        }

    }

    private companion object {
        private val stopEstimateDiffCallback = object : DiffUtil.ItemCallback<StopEstimateVo>() {
            override fun areItemsTheSame(oldItem: StopEstimateVo, newItem: StopEstimateVo): Boolean =
                oldItem.vehicle == newItem.vehicle

            override fun areContentsTheSame(oldItem: StopEstimateVo, newItem: StopEstimateVo): Boolean =
                oldItem == newItem
        }
    }

}