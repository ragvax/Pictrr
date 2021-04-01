package com.ragvax.picttr.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ragvax.picttr.data.topic.model.Topic
import com.ragvax.picttr.databinding.ItemGalleryTagBinding

class GalleryTopicsAdapter(
    private val topics: List<Topic>,
    private val listener: OnItemClickListener,
    ) : RecyclerView.Adapter<GalleryTopicsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemGalleryTagBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = topics[position].id
                    listener.onItemClick(item)
                }
            }
        }

        fun bind(recommendationText: String) {
            binding.tvItem.text = recommendationText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGalleryTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendationText = topics[position].title
        holder.bind(recommendationText)
    }

    override fun getItemCount() = topics.size

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}