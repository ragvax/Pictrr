package com.ragvax.picttr.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.databinding.ItemPhotoBinding
import com.ragvax.picttr.utils.loadGridPhotoUrl

class GalleryAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Photo, GalleryAdapter.GalleryViewHolder>(PhotoDiffCallback()) {

    inner class GalleryViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parentItemPhotoConstraint.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.onPhotoClick(item)
                }
            }
        }

        fun bind(photo: Photo) = with(binding) {
            itemImageView.loadGridPhotoUrl(photo.urls.small, photo.color)
            setImageDimensionRatio(binding, calculateImageDimensionRatio(photo))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo)
        }
    }

    interface OnItemClickListener {
        fun onPhotoClick(photo: Photo)
    }

    private class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
    }

    private fun calculateImageDimensionRatio(photo: Photo): String {
        return if (photo.width.toFloat() / photo.height.toFloat() > 1.8) {
            String.format("4000:3000")
        } else {
            String.format("%d:%d", photo.width, photo.height)
        }
    }

    private fun setImageDimensionRatio(binding: ItemPhotoBinding, ratio: String) {
        val set = ConstraintSet()
        binding.apply {
            set.clone(parentItemPhotoConstraint)
            set.setDimensionRatio(itemImageView.id, ratio)
            set.applyTo(parentItemPhotoConstraint)
        }
    }
}
