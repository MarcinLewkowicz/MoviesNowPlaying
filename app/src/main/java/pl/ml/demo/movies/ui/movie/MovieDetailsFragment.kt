package pl.ml.demo.movies.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        setupViews(binding)
        return binding.root
    }

    private fun setupViews(binding: FragmentMovieDetailsBinding) {
        val movie = args.movie
        with (binding) {
            movieImage.load(getString(R.string.images_base_url) + movie.backdropPath) {
                placeholder(R.color.image_placeholder)
                fallback(R.color.image_placeholder)
            }
            movieTitle.text = movie.title
            movieOverview.text = movie.overview
            movieDate.text = movie.releaseDate
            movieRate.text = getString(R.string.detail_rating_label, movie.voteAverage)
        }
    }

}