package com.congvtt1.smartmovie.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congvtt1.domain.model.Actor
import com.congvtt1.domain.model.Genre
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.databinding.ItemGenreBinding
import com.congvtt1.smartmovie.movie.common.MovieConstant

class GenreAdapter(private val onItemClicked: (Genre) -> Unit) :
    ListAdapter<Genre, GenreAdapter.GenreHolder>(GenreDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        return GenreHolder(
            ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            onItemClicked(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.onBindViewHolder(getItem(position))
    }

    class GenreDiffUtil : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.copy(id = 0) == newItem.copy(id = 0)
        }
    }

    class GenreHolder(val binding: ItemGenreBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemGenre.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun onBindViewHolder(genre: Genre) {
            binding.tvGenre.text = genre.name
            Glide.with(binding.ivImage)
                .load(MovieConstant.MAP_GENRE_TO_IMAGE[genre.id])
                .centerCrop()
                .error(R.drawable.default_movie)
                .placeholder(R.drawable.default_movie)
                .into(binding.ivImage)
        }
    }
}
