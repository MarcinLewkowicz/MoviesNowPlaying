package pl.ml.demo.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.FragmentMoviesListBinding
import pl.ml.demo.movies.ui.utils.MarginItemDecoration

class MoviesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        val columnCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        binding.moviesList.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        binding.moviesList.adapter = MoviesRecyclerViewAdapter(PlaceholderContent.ITEMS)
        binding.moviesList.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_margin), columnCount))
        return binding.root
    }

}