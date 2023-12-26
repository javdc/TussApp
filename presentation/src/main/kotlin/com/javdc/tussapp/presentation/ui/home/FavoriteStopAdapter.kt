package com.javdc.tussapp.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.domain.model.FavoriteStopBo
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowFavoriteStopBinding

class FavoriteStopAdapter(private val listener: (Int, View) -> Unit) : ListAdapter<FavoriteStopBo, FavoriteStopAdapter.ViewHolder>(favoriteStopDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowFavoriteStopBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item.code, holder.itemView) }
    }

    inner class ViewHolder(private val binding: RowFavoriteStopBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoriteStopBo) {
            binding.apply {
                root.transitionName = root.context.getString(R.string.row_favorite_stop_transition_name, item.code)
                favoriteStopButton.text = item.code.toString()
            }
        }

    }

    private companion object {
        private val favoriteStopDiffCallback = object : DiffUtil.ItemCallback<FavoriteStopBo>() {
            override fun areItemsTheSame(oldItem: FavoriteStopBo, newItem: FavoriteStopBo): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: FavoriteStopBo, newItem: FavoriteStopBo): Boolean =
                oldItem == newItem
        }
    }
}