package pl.ml.demo.movies.ui.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import pl.ml.demo.movies.data.repository.FavoritesRepository
import pl.ml.demo.movies.domain.model.MovieScreenItem
import pl.ml.demo.movies.domain.usecase.GetMoviesListUseCase
import pl.ml.demo.movies.domain.usecase.MapToMovieScreenItemUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val mapToScreenMovieScreenItem: MapToMovieScreenItemUseCase,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val TAG = "MoviesViewModel"
    private val MIN_QUERY_LENGTH = 3
    private val QUERY_THROTTLE_TIME = 500.milliseconds

    private var query = MutableStateFlow<String?>(null)
    private val refreshPagedDataFlow = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = query
        .filter { it.isNullOrEmpty() || it.length >= MIN_QUERY_LENGTH }
        .transformLatest {
            // Delay and throttle query to avoid fast consecutive requests to backend when user is still typing
            if (it?.isNotEmpty() == true) delay(QUERY_THROTTLE_TIME)
            emit(it)
        }
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                MoviesPagingSource(getMoviesListUseCase, it)
            }.flow
        }
        .cachedIn(viewModelScope)
        // Map the data again on every update requested UI refresh (without fetching the data from backend again)
        .combine(refreshPagedDataFlow) { data, _ ->
            data.map {
                mapToScreenMovieScreenItem(it)
            }
        }
        // This is needed again otherwise submitData gets stuck with whole flow causing favorites button not working.
        .cachedIn(viewModelScope)

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    private fun updateContent() {
        viewModelScope.launch {
            refreshPagedDataFlow.emit(Unit)
        }
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