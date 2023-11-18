package pl.ml.demo.movies.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.FragmentMovieDetailsBinding
import pl.ml.demo.movies.ui.utils.viewLifecycleScope

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        setupViews(binding)
        collectState(binding)
        return binding.root
    }

    private fun setupViews(binding: FragmentMovieDetailsBinding) {
        binding.toolbar.menu.findItem(R.id.action_favorite).setOnMenuItemClickListener {
            viewModel.onItemFavoriteClicked()
            true
        }
    }

    private fun collectState(binding: FragmentMovieDetailsBinding) {
        viewLifecycleScope.launch {
            viewModel.state.collect {
                val movie = it.movieScreenItem
                with(binding) {
                    movieImage.load(movie.backdropUrl) {
                        placeholder(R.color.image_placeholder)
                        fallback(R.color.image_placeholder)
                        error(R.color.image_placeholder)
                    }
                    movieTitle.text = movie.title
                    movieOverview.text = movie.overview
                    movieDate.text = getString(R.string.movie_details_date_label, movie.releaseDate)
                    movieRate.text = getString(R.string.movie_details_rating_label, movie.voteAverage)
                    toolbar.menu.findItem(R.id.action_favorite)
                        .setIcon(if (movie.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star)
                }
            }
        }
    }

}