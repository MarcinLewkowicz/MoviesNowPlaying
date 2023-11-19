package pl.ml.demo.movies.ui.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import pl.ml.demo.movies.data.repository.FavoritesRepository
import pl.ml.demo.movies.domain.model.MovieScreenItem
import pl.ml.demo.movies.domain.usecase.GetMoviesListUseCase
import pl.ml.demo.movies.domain.usecase.MapToMovieScreenItemsListUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val mapToScreenMoviesListUseCase: MapToMovieScreenItemsListUseCase,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val TAG = "MoviesViewModel"
    private val MIN_QUERY_LENGTH = 3
    private val QUERY_THROTTLE_TIME = 500.milliseconds

    private var query = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = query
        .filter { it.isNullOrEmpty() || it.length >= MIN_QUERY_LENGTH }
        .transformLatest {
            if (it?.isNotEmpty() == true) delay(QUERY_THROTTLE_TIME)
            emit(it)
        }
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                MoviesPagingSource(getMoviesListUseCase, mapToScreenMoviesListUseCase, it)
            }.flow
        }
        .cachedIn(viewModelScope)

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    private fun updateContent() {
//        val screenItems = mapToScreenMoviesListUseCase(cachedItems)
//        _state.value = Content(screenItems)
    }

    fun onQueryChanged(newText: String?) {
        Log.d(TAG, "onQueryChanged() => $newText")
        query.value = newText
    }

    fun onItemClicked(movie: MovieScreenItem) {
        viewModelScope.launch {
            _action.emit(Action.NavigateToDetails(movie))
        }
    }

    fun onItemFavoriteClicked(movieId: Int) {
        favoritesRepository.toggleFavoriteMovie(movieId)
        updateContent()
    }

    fun onResume() {
        updateContent()
    }

    sealed class Action {
        data class NavigateToDetails(val movie: MovieScreenItem) : Action()
    }

}