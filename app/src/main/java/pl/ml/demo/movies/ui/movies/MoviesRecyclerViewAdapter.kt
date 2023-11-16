package pl.ml.demo.movies.ui.movies

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import pl.ml.demo.movies.R

import pl.ml.demo.movies.ui.movies.PlaceholderContent.PlaceholderItem
import pl.ml.demo.movies.databinding.ItemMovieBinding

class MoviesRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.movieItemNumber
        val contentView: TextView = binding.movieContent

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}