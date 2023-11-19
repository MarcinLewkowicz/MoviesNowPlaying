package pl.ml.demo.movies.ui.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.data.util.Result
import pl.ml.demo.movies.domain.usecase.GetMoviesListUseCase

class MoviesPagingSource(
    private val getMoviesListUseCase: GetMoviesListUseCase,
    private val query: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val response = getMoviesListUseCase(query, pageNumber)
            when (response) {
                is Result.Success -> {
                    LoadResult.Page(
                        data = response.data,
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }
}