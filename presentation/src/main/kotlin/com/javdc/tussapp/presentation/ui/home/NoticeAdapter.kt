package com.javdc.tussapp.presentation.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.presentation.databinding.RowNoticeBigImageBinding
import com.javdc.tussapp.presentation.databinding.RowNoticeBinding

class NoticeAdapter: ListAdapter<NoticeBo, NoticeAdapter.NoticeViewHolder>(noticeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        return when (viewType) {
            NOTICE_TYPE_NORMAL -> NormalNoticeViewHolder(
                RowNoticeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            NOTICE_TYPE_BIG_IMAGE -> BigImageNoticeViewHolder(
                RowNoticeBigImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.bigImage) NOTICE_TYPE_BIG_IMAGE else NOTICE_TYPE_NORMAL
    }

    abstract class NoticeViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: NoticeBo)
    }

    inner class NormalNoticeViewHolder(private val binding: RowNoticeBinding) : NoticeViewHolder(binding) {
        override fun bind(item: NoticeBo) {
            binding.apply {
                rowNoticeTitle.text = item.title
                rowNoticeDescription.text = item.description
                item.imageBase64
                    ?.let { tryOrNull { Base64.decode(it, Base64.DEFAULT) } }
                    ?.let { imageByteArray ->
                        rowNoticeIconImageView.isVisible = true
                        Glide
                            .with(root.context)
                            .load(imageByteArray)
                            .into(rowNoticeIconImageView)
                    } ?: run {
                        rowNoticeIconImageView.isVisible = false
                    }
                item.url?.let { url ->
                    root.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        ContextCompat.startActivity(root.context, intent, null)
                    }
                }
            }
        }
    }

    inner class BigImageNoticeViewHolder(private val binding: RowNoticeBigImageBinding) : NoticeViewHolder(binding) {
        override fun bind(item: NoticeBo) {
            binding.apply {
                item.imageBase64
                    ?.let { tryOrNull { Base64.decode(it, Base64.DEFAULT) } }
                    ?.let { imageByteArray ->
                        Glide
                            .with(root.context)
                            .load(imageByteArray)
                            .into(root)
                    }

                item.url?.let { url ->
                    root.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        ContextCompat.startActivity(root.context, intent, null)
                    }
                }
            }
        }
    }

    private companion object {
        private const val NOTICE_TYPE_NORMAL = 0
        private const val NOTICE_TYPE_BIG_IMAGE = 1

        private val noticeDiffCallback = object : DiffUtil.ItemCallback<NoticeBo>() {
            override fun areItemsTheSame(oldItem: NoticeBo, newItem: NoticeBo): Boolean =
                oldItem.noticeId == newItem.noticeId

            override fun areContentsTheSame(oldItem: NoticeBo, newItem: NoticeBo): Boolean =
                oldItem == newItem
        }
    }
}