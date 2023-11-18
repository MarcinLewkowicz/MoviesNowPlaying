package pl.ml.demo.movies.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pl.ml.demo.movies.domain.usecase.ToggleFavoriteMovieUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val navArgs = MovieDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _state = MutableStateFlow(MovieDetailsScreenState(navArgs.movie))
    val state: Flow<MovieDetailsScreenState> = _state


    fun onItemFavoriteClicked() {
        toggleFavoriteMovieUseCase(navArgs.movie)
        // Temporary workaround since we have a MovieScreenItem not a Movie and there is no cached Movies in repository,
        // so we can't use MapToMovieScreenItemUseCase to apply updated favorite flag.
        // TODO - do it better.
        val movie = _state.value.movieScreenItem
        _state.value = MovieDetailsScreenState(movie.copy(isFavorite = !movie.isFavorite))
    }

}