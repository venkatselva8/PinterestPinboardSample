package app.task.pinterestsample.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*
 * Created by venkatesh on 15-Apr-19.
 */

abstract class EndlessRecyclerViewScrollListener(var linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    // The minimum number of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        onScrollStateChanged(
            if (newState == RecyclerView.SCROLL_STATE_IDLE) AppConstant.RECYCLERVIEW_SCROLL_ON_STOP
            else AppConstant.RECYCLERVIEW_SCROLL_ON_START
        )
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            onLoadMore()
        }
    }

    abstract fun onScrollStateChanged(scrollState: Int)

    abstract fun onLoadMore()

}