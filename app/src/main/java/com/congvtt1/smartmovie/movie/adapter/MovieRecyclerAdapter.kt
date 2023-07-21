package com.congvtt1.smartmovie.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congvtt1.domain.model.Movie
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.databinding.ItemMovieGridBinding
import com.congvtt1.smartmovie.databinding.ItemMovieMenuBinding
import com.congvtt1.smartmovie.movie.common.MovieConstant

class MovieRecyclerAdapter(
    private val onItemClicked: (Movie) -> Unit
) : ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffUtil()) {

    private var viewType: Int = MovieConstant.VIEW_TYPE_GRID

    fun setViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MovieConstant.VIEW_TYPE_MENU) {
            MovieMenuHolder(
                ItemMovieMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ) {
                onItemClicked(getItem(it))
            }
        } else {
            MovieGridHolder(
                ItemMovieGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            ) {
                onItemClicked(getItem(it))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieMenuHolder) {
            holder.onBindViewHolder(getItem(position))
        } else if (holder is MovieGridHolder) {
            holder.onBindViewHolder(getItem(position))
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            bundle.keySet().forEach {
                when (it) {
                    "title" -> {
                        val title = bundle.getString(it)
                        if (holder is MovieMenuHolder) {
                            holder.binding.tvMovieName.text = title
                        } else if (holder is MovieGridHolder) {
                            holder.binding.tvMovieName.text = title
                        }
                    }
                    "overview" -> {
                        if (holder is MovieMenuHolder) {
                            holder.binding.tvMovieName.text = bundle.getString(it)
                        }
                    }
                    "posterPath" -> {
                        val posterPath = bundle.getString(it)
                        if (holder is MovieMenuHolder) {
                            Glide.with(holder.binding.ivImage)
                                .load(posterPath)
                                .fitCenter()
                                .error(R.drawable.default_movie)
                                .placeholder(R.drawable.default_movie)
                                .into(holder.binding.ivImage)
                        } else if (holder is MovieGridHolder) {
                            Glide.with(holder.binding.ivImage)
                                .load(posterPath)
                                .fitCenter()
                                .error(R.drawable.default_movie)
                                .placeholder(R.drawable.default_movie)
                                .into(holder.binding.ivImage)
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    class MovieGridHolder(val binding: ItemMovieGridBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemMovieGrid.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }
        fun onBindViewHolder(movie: Movie) {
            Glide.with(binding.ivImage)
                .load(movie.posterPath)
                .fitCenter()
                .error(R.drawable.default_movie)
                .placeholder(R.drawable.default_movie)
                .into(binding.ivImage)

            binding.tvMovieName.text = movie.originalTitle
        }
    }

    class MovieMenuHolder(val binding: ItemMovieMenuBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemMovieMenu.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }
        fun onBindViewHolder(movie: Movie) {
            Glide.with(binding.ivImage)
                .load(movie.posterPath)
                .fitCenter()
                .error(R.drawable.default_movie)
                .placeholder(R.drawable.default_movie)
                .into(binding.ivImage)

            binding.tvMovieName.text = movie.originalTitle
            binding.tvDescription.text = movie.overview
        }
    }
}
