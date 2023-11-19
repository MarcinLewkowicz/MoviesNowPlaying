package pl.ml.demo.movies.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pl.ml.demo.movies.R
import pl.ml.demo.movies.databinding.ItemMovieBinding
import pl.ml.demo.movies.domain.model.MovieScreenItem

private const val KEY_FAVORITE = "KEY_FAVORITE"

class MoviesRecyclerViewAdapter(
    private val onItemClicked: (MovieScreenItem) -> Unit,
    private val onItemFavoriteClicked: (Int) -> Unit
) : PagingDataAdapter<MovieScreenItem, MoviesRecyclerViewAdapter.ViewHolder>(DiffCallback()) {

    private val TAG = "MoviesRecyclerViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")
        val item = getItem(position)
        holder.bindAll(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        Log.d(TAG, "onBindViewHolder() at $position, payloads => $payloads")
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val item = getItem(position)
            if (item != null) {
                holder.bindFavorite(item)
            }
        }
    }

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.moviePicture
        private val titleView: TextView = binding.movieTitle
        private val favoriteView: ImageView = binding.favoriteIcon

        fun bindAll(item: MovieScreenItem?) {
            imageView.load(item?.posterUrl) {
                placeholder(R.color.image_placeholder)
                fallback(R.color.image_placeholder)
                error(R.color.image_placeholder)
            }
            titleView.text = item?.title ?: "-----"
            if (item != null) {
                favoriteView.isVisible = true
                favoriteView.isSelected = item.isFavorite
                favoriteView.setOnClickListener {
                    onItemFavoriteClicked(item.id)
                }
                itemView.setOnClickListener {
                    onItemClicked(item)
                }
            } else {
                favoriteView.isVisible = false
                itemView.setOnClickListener(null)
            }
        }

        fun bindFavorite(item: MovieScreenItem) {
            favoriteView.isSelected = item.isFavorite
            // Update the item in the listener otherwise old copy from bindAll() (with another favorite state) will be passed to callback ad then to detaild screen.
            // The other solution would be to store item in view holder and reference it instead of the local copy.
            // Another solution is to pass only movie ID, but currently there is no cache in the app, and there is no loading details on details screen.
            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieScreenItem>() {

        override fun areItemsTheSame(oldItem: MovieScreenItem, newItem: MovieScreenItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieScreenItem, newItem: MovieScreenItem): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: MovieScreenItem, newItem: MovieScreenItem): Any? {
            val set = mutableSetOf<Any>()
            if (newItem.isFavorite != oldItem.isFavorite) {
                set += KEY_FAVORITE
            }
            return if (set.isEmpty()) null else set
        }

    }

}