package pl.ml.demo.movies.ui.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.repository.MoviesRepository
import pl.ml.demo.movies.data.util.Result
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Content
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Error
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Loading
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val TAG = "MoviesViewModel"
    private val MIN_QUERY_LENGTH = 3

    private val _state = MutableStateFlow<MoviesScreenState>(Loading)
    val state: Flow<MoviesScreenState> = _state

    private var query: String = ""
    private var queryReloadJob: Job? = null

    init {
        loadContent()
    }

    fun loadContent() {
        Log.d(TAG, "loadContent() => query: $query")
        viewModelScope.launch {
            _state.value = Loading
            val result = if (query.isEmpty()) {
                repository.getMoviesNowPlaying()
            } else {
                repository.searchMovies(query)
            }
            when (result) {
                is Result.Success -> {
                    _state.value = Content(result.data)
                }
                is Result.Error -> {
                    // TODO - change to user-friendly info
                    _state.value = Error(result.exception.message ?: "")
                }
            }
        }
    }

    fun onQueryChanged(newText: String?) {
        Log.d(TAG, "onQueryChanged() => $newText")
        // If the query is empty, reload content right away.
        if (newText.isNullOrEmpty()) {
            queryReloadJob?.cancel()
            query = ""
            loadContent()
            return
        }
        // Don't make a search when query is too short
        if (newText.length >= MIN_QUERY_LENGTH) {
            queryReloadJob?.cancel()
            // Throttle searching
            queryReloadJob = viewModelScope.launch {
                delay(500.milliseconds)
                query = newText
                loadContent()
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
    }

}