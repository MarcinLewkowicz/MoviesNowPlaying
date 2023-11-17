package pl.ml.demo.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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

    private val TAG = "MoviesFragment"
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MoviesRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        setupViews(binding)
        setupSearchView(binding)
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
        val imageBaseUrl = getString(R.string.images_base_url)
        adapter = MoviesRecyclerViewAdapter(imageBaseUrl)
        binding.moviesList.adapter = adapter
    }

    private fun setupSearchView(binding: FragmentMoviesListBinding) {
        val item = binding.moviesToolbar.menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as? SearchView
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView?.inputType = EditorInfo.TYPE_CLASS_TEXT + EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE
        searchView?.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do nothing
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                viewModel.onQueryChanged(newText)
                return true
            }
        })
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
        }
    }

}