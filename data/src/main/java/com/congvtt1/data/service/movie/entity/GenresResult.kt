package com.congvtt1.data.service.movie.entity

import com.google.gson.annotations.SerializedName

data class GenresResult(
    @field:SerializedName("genres") val genres: List<Genre> = emptyList(),
)