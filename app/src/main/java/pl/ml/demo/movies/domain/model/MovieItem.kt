package pl.ml.demo.movies.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val posterUrl: String,
    val releaseDate: String,
    val voteAverage: String,
    val isFavorite: Boolean,
) : Parcelable