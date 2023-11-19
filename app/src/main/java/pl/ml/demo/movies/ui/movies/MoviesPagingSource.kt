package pl.ml.demo.movies.ui.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.ml.demo.movies.data.util.Result
import pl.ml.demo.movies.domain.model.MovieScreenItem
import pl.ml.demo.movies.domain.usecase.GetMoviesListUseCase
import pl.ml.demo.movies.domain.usecase.MapToMovieScreenItemsListUseCase

class MoviesPagingSource(
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val mapToMovieScreenItemsListUseCase: MapToMovieScreenItemsListUseCase,
    private val query: String?
) : PagingSource<Int, MovieScreenItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieScreenItem> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val response = getMoviesListUseCase(query, pageNumber)
            when (response) {
                is Result.Success -> {
                    LoadResult.Page(
                        data = mapToMovieScreenItemsListUseCase(response.data),
                        prevKey = null, // Only paging forward.
                        nextKey = pageNumber + 1
                    )
                }
                is Result.Error -> LoadResult.Error(response.exception)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieScreenItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }
}