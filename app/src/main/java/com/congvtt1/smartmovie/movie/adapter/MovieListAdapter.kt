package com.congvtt1.smartmovie.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congvtt1.domain.model.Genre
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.databinding.ItemMovieListBinding
import com.congvtt1.smartmovie.movie.common.MovieConstant

class MovieListAdapter(private val onItemClicked: (Movie) -> Unit) :
    ListAdapter<Movie, MovieListAdapter.MovieListHolder>(MovieListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListHolder {
        return MovieListHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            onItemClicked(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: MovieListHolder, position: Int) {
        holder.onBindViewHolder(getItem(position))
    }

    class MovieListDiffUtil : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.copy(id = 0) == newItem.copy(id = 0)
        }
    }

    class MovieListHolder(val binding: ItemMovieListBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemMovie.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun onBindViewHolder(movie: Movie) {
            Glide.with(binding.ivImage)
                .load(movie.backdropPath)
                .error(R.drawable.default_movie)
                .placeholder(R.drawable.default_movie)
                .centerCrop()
                .into(binding.ivImage)

            binding.tvMovieName.text = movie.title
            binding.tvCategory.text = movie.category
            binding.rating.rating = (movie.voteAverage / 2).toFloat()
        }
    }
}
