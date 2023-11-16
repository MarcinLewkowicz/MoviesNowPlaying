package pl.ml.demo.movies.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***
 * RecyclerView item decoration providing even items spacing size for both lists and grids.
 */
class MarginItemDecoration(private val space: Int, private val columnCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position < columnCount) {
            outRect.top = space
        }
        if (position % columnCount == 0) {
            outRect.left = space
        } else {
            outRect.left = space / 2
        }
        if (position % columnCount == columnCount - 1) {
            outRect.right = space
        } else {
            outRect.right = space / 2
        }
        outRect.bottom = space
    }
}