package pl.ml.demo.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.FragmentMoviesListBinding
import pl.ml.demo.movies.ui.utils.MarginItemDecoration

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val adapter = MoviesRecyclerViewAdapter()
    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        setupViews(binding)
        collectState(binding)
        return binding.root
    }

    private fun setupViews(binding: FragmentMoviesListBinding) {
        val columnCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        binding.moviesList.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        binding.moviesList.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_margin), columnCount)
        )
        binding.moviesList.adapter = adapter
    }

    private fun collectState(binding: FragmentMoviesListBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                binding.moviesList.isVisible = it is MoviesScreenState.Content
                binding.errorView.isVisible = it is MoviesScreenState.Error
                binding.loadingBar.isVisible = it is MoviesScreenState.Loading
                when (it) {
                    is MoviesScreenState.Content -> {
                        adapter.setValues(it.movies)
                    }
                    is MoviesScreenState.Error -> {
                        binding.errorView.text = it.errorMessage
                    }
                    MoviesScreenState.Loading -> { /* nothing else to do */ }
                }
            }
            binding.moviesList.adapter = MoviesRecyclerViewAdapter()
        }
    }

}