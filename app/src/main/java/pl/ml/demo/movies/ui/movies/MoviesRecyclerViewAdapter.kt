package pl.ml.demo.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pl.ml.demo.movies.R
import pl.ml.demo.movies.data.model.Movie
import pl.ml.demo.movies.databinding.ItemMovieBinding

class MoviesRecyclerViewAdapter(
    private val imageBaseUrl: String,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    private var values: List<Movie> = emptyList()

    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val url =  imageBaseUrl + item.backdropPath
        holder.imageView.load(url) {
            placeholder(R.drawable.ic_launcher_background)
            fallback(R.drawable.ic_launcher_background)
        }
        holder.contentView.text = item.title
        holder.itemView.setOnClickListener {
            onItemClicked(item.id)
        }
    }

    fun setValues(newValues: List<Movie>) {
        val callback = DiffCallback(values, newValues)
        val diff = DiffUtil.calculateDiff(callback)
        values = newValues
        diff.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.movieItemNumber
        val contentView: TextView = binding.movieContent
    }

    inner class DiffCallback(private val values: List<Movie>, private val newValues: List<Movie>) : Callback() {

        override fun getOldListSize(): Int = values.size

        override fun getNewListSize(): Int = newValues.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            values[oldItemPosition].id == newValues[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            values[oldItemPosition] == newValues[newItemPosition]

    }

}