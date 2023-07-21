package com.congvtt1.smartmovie.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congvtt1.domain.model.Actor
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.databinding.ItemCastBinding

class CastAdapter : ListAdapter<Actor, CastAdapter.CastHolder>(ActorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        return CastHolder(
            ItemCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        holder.onBindViewHolder(getItem(position))
    }

    class ActorDiffUtil : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class CastHolder(val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindViewHolder(cast: Actor) {
            binding.tvName.text = cast.name
            Glide.with(binding.ivImage)
                .load(cast.image)
                .centerCrop()
                .error(R.drawable.default_movie)
                .placeholder(R.drawable.default_movie)
                .into(binding.ivImage)
        }
    }
}
