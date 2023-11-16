package pl.ml.demo.movies.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pl.ml.demo.movies.data.repository.MoviesRepository
import pl.ml.demo.movies.data.util.Result
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Content
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Error
import pl.ml.demo.movies.ui.movies.MoviesScreenState.Loading
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MoviesScreenState>(Loading)
    val state: Flow<MoviesScreenState> = _state

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _state.value = Loading
            val result = repository.getMoviesNowPlaying()
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

}