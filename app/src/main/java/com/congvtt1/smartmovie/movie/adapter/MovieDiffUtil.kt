package com.congvtt1.smartmovie.movie.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.congvtt1.domain.model.Movie

class MovieDiffUtil : ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.copy(id = 0) == newItem.copy(id = 0)
    }

    override fun getChangePayload(oldItem: Movie, newItem: Movie): Any {
        return Bundle().putMovieData(oldItem, newItem)
    }

    private fun Bundle.putMovieData(oldItem: Movie, newItem: Movie): Bundle {
        if (oldItem.adult != newItem.adult) {
            putBoolean("adult", newItem.adult)
        }
        if (oldItem.backdropPath != newItem.backdropPath) {
            putString("backdropPath", newItem.backdropPath)
        }
        if (oldItem.genreIDS != newItem.genreIDS) {
            putString("genreIDS", newItem.genreIDS.joinToString { "," })
        }
        if (oldItem.id != newItem.id) {
            putLong("id", newItem.id)
        }
        if (oldItem.originalLanguage != newItem.originalLanguage) {
            putString("originalLanguage", newItem.originalLanguage)
        }
        if (oldItem.originalTitle != newItem.originalTitle) {
            putString("originalTitle", newItem.originalTitle)
        }
        if (oldItem.overview != newItem.overview) {
            putString("overview", newItem.overview)
        }
        if (oldItem.popularity != newItem.popularity) {
            putDouble("popularity", newItem.popularity)
        }
        if (oldItem.posterPath != newItem.posterPath) {
            putString("posterPath", newItem.posterPath)
        }
        if (oldItem.releaseDate != newItem.releaseDate) {
            putString("releaseDate", newItem.releaseDate)
        }
        if (oldItem.title != newItem.title) {
            putString("title", newItem.title)
        }
        if (oldItem.video != newItem.video) {
            putBoolean("video", newItem.video)
        }
        if (oldItem.voteAverage != newItem.voteAverage) {
            putDouble("voteAverage", newItem.voteAverage)
        }
        if (oldItem.voteCount != newItem.voteCount) {
            putLong("voteCount", newItem.voteCount)
        }
        return this
    }
}