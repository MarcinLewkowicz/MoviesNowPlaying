package pl.ml.demo.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.FragmentMoviesListBinding
import pl.ml.demo.movies.ui.MainNavigationInterface
import pl.ml.demo.movies.ui.utils.viewLifecycleScope

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


    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun setupViews(binding: FragmentMoviesListBinding) {
        val columnCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        binding.moviesList.layoutManager = GridLayoutManager(context, columnCount)
        adapter = MoviesRecyclerViewAdapter(viewModel::onItemClicked, viewModel::onItemFavoriteClicked)
        binding.moviesList.adapter = adapter
        binding.errorViewRetryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun setupSearchView(binding: FragmentMoviesListBinding) {
        val searchItem = binding.moviesToolbar.menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView?.inputType = EditorInfo.TYPE_CLASS_TEXT + EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE
        searchView?.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do nothing
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!searchView.isIconified) {
                    Log.d(TAG, "onQueryTextChange: $newText")
                    viewModel.onQueryChanged(newText)
                }
                return true
            }
        })
        setupOnBackPressCallback(binding, searchItem)
    }

    private fun setupOnBackPressCallback(
        binding: FragmentMoviesListBinding,
        searchItem: MenuItem?
    ) {
        val onBackPressedCallback = LocalOnBackPressedCallback(binding)
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
        searchItem?.setOnActionExpandListener(object : OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                Log.d(TAG, "onMenuItemActionExpand")
                onBackPressedCallback.isEnabled = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                Log.d(TAG, "onMenuItemActionCollapse")
                onBackPressedCallback.isEnabled = false
                return true
            }
        })
    }

    private fun collectState(binding: FragmentMoviesListBinding) {
        viewLifecycleScope.launch {
            viewModel.state.collect {
                adapter.submitData(it)
            }
        }
        viewLifecycleScope.launch {
            adapter.loadStateFlow.collect {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
                binding.errorViewRetryButton.isVisible = it.refresh is LoadState.Error
                binding.errorViewText.text = (it.refresh as? LoadState.Error)?.error?.message ?: ""
                binding.errorView.isVisible = it.refresh is LoadState.Error
            }
        }
        viewLifecycleScope.launch {
            viewModel.action.collect {
                when (it) {
                    is MoviesViewModel.Action.NavigateToDetails -> navigateToMovieDetails(it)
                }
            }
        }
    }

    private fun navigateToMovieDetails(it: MoviesViewModel.Action.NavigateToDetails) {
        (activity as? MainNavigationInterface)?.navigateToMovieDetails(it.movie)
    }

    inner class LocalOnBackPressedCallback(private val binding: FragmentMoviesListBinding) : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed")
            val item = binding.moviesToolbar.menu?.findItem(R.id.action_search)
            item?.collapseActionView()
        }
    }

}