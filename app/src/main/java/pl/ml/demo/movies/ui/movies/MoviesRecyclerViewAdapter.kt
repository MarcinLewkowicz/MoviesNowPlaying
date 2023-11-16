package pl.ml.demo.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.ml.demo.movies.R
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.databinding.ItemMovieBinding

class MoviesRecyclerViewAdapter : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    private val values: List<Movie> = emptyList()

    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.contentView.text = item.title
    }

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.movieItemNumber
        val contentView: TextView = binding.movieContent
    }

}