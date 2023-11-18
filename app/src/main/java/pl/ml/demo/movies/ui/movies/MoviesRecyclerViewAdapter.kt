package pl.ml.demo.movies.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.ItemMovieBinding
import pl.ml.demo.movies.domain.model.MovieScreenItem


class MoviesRecyclerViewAdapter(
    private val onItemClicked: (MovieScreenItem) -> Unit,
    private val onItemFavoriteClicked: (MovieScreenItem) -> Unit
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "MoviesRecyclerViewAdapter"
    private var values: List<MovieScreenItem> = emptyList()
    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")
        val item = values[position]
        holder.imageView.load(item.posterUrl) {
            placeholder(R.color.image_placeholder)
            fallback(R.color.image_placeholder)
            error(R.color.image_placeholder)
        }
        holder.titleView.text = item.title
        holder.favoriteView.isSelected = item.isFavorite
        holder.favoriteView.setOnClickListener {
            onItemFavoriteClicked(item)
        }
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        Log.d(TAG, "onBindViewHolder() at $position, payloads => $payloads")
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val item = values[position]
            holder.favoriteView.isSelected = item.isFavorite
        }
    }

    fun setValues(newValues: List<MovieScreenItem>) {
        val callback = DiffCallback(values, newValues)
        val diff = DiffUtil.calculateDiff(callback)
        values = newValues
        diff.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.moviePicture
        val titleView: TextView = binding.movieTitle
        val favoriteView: ImageView = binding.favoriteIcon
    }

    private val KEY_FAVORITE = "KEY_FAVORITE"

    inner class DiffCallback(private val oldValues: List<MovieScreenItem>, private val newValues: List<MovieScreenItem>) : Callback() {

        override fun getOldListSize(): Int = oldValues.size

        override fun getNewListSize(): Int = newValues.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldValues[oldItemPosition].id == newValues[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldValues[oldItemPosition] == newValues[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): MutableSet<Any>? {
            val newItem= newValues[newItemPosition]
            val oldItem = oldValues[oldItemPosition]
            val set = mutableSetOf<Any>()
            if (newItem.isFavorite != oldItem.isFavorite) {
                set += KEY_FAVORITE
            }
            return if (set.isEmpty()) null else set
        }

    }

}