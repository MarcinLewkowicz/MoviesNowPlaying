package pl.ml.demo.movies.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.ActivityMainBinding
import pl.ml.demo.movies.domain.model.MovieScreenItem
import pl.ml.demo.movies.ui.movies.MoviesFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainNavigationInterface {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    override fun navigateToMovieDetails(movie: MovieScreenItem) {
        val direction = MoviesFragmentDirections.showMovieDetailsFragment(movie)
        navController.navigate(direction)
    }

}