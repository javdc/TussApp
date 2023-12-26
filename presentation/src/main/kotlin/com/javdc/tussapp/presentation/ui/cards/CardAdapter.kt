package com.javdc.tussapp.presentation.ui.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.RowCardBinding
import com.javdc.tussapp.presentation.model.CardVo
import com.javdc.tussapp.presentation.util.formatCardNumberToString

class CardAdapter(private val onClickEdit: (CardVo) -> Unit, private val onClickDelete: (CardVo) -> Unit) : ListAdapter<CardVo, CardAdapter.ViewHolder>(cardDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: RowCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardVo) {
            setUpCardInfo(item)
            setUpCardButtons(item)
            setUpCardStyle(item)
        }

        private fun setUpCardInfo(item: CardVo) {
            binding.apply {
                cardNumberTextView.text = item.cardNumber.formatCardNumberToString()
                cardCustomNameTextView.text = item.customName
                cardTitleTextView.text = item.passName
                cardBalanceTextView.text = item.eurosBalance?: item.tripsBalance?.let { root.context.resources.getQuantityString(R.plurals.card_trips_format, it, it) }
            }
        }

        private fun setUpCardButtons(item: CardVo) {
            binding.apply {
                cardEditButton.setOnClickListener { onClickEdit(item) }
                cardDeleteButton.setOnClickListener { onClickDelete(item) }
            }
        }

        private fun setUpCardStyle(item: CardVo) {
            binding.apply {
                cardCardView.setCardBackgroundColor(item.primaryColorInt)
                cardBottomColorView.setBackgroundColor(item.secondaryColorInt)
                cardEditButton.setColorFilter(item.secondaryColorInt)
                cardDeleteButton.setColorFilter(item.secondaryColorInt)
                cardBalanceTextView.setTextColor(item.secondaryColorInt)
                cardNumberTextView.setTextColor(item.primaryColorInt)
                cardCustomNameTextView.setTextColor(item.secondaryColorInt)
                cardTitleTextView.setTextColor(item.primaryColorInt)
            }
        }

    }

    private companion object {
        private val cardDiffCallback = object : DiffUtil.ItemCallback<CardVo>() {
            override fun areItemsTheSame(oldItem: CardVo, newItem: CardVo): Boolean =
                oldItem.cardNumber == newItem.cardNumber

            override fun areContentsTheSame(oldItem: CardVo, newItem: CardVo): Boolean =
                oldItem == newItem
        }
    }
}
