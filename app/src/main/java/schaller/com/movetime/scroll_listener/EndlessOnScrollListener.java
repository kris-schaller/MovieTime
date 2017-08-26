package schaller.com.movetime.scroll_listener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Will notify any listeners implementing {@link OnLoadMoreListener} that the list has passed the
 * threshold and should execute loading anymore items.
 */
public class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private static final int VISIBLE_THRESHOLD = 5;
    // Sets the starting page index
    private static final int STARTING_PAGE_INDEX = 0;


    // Threshold based on number of spans
    private int visibleThreshold = VISIBLE_THRESHOLD;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the data set after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    private OnLoadMoreListener listener;
    private RecyclerView.LayoutManager mLayoutManager;

    public interface OnLoadMoreListener {

        void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }

    public EndlessOnScrollListener(@NonNull LinearLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        this.listener = listener;
    }

    public EndlessOnScrollListener(@NonNull GridLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.listener = listener;
    }

    public EndlessOnScrollListener(@NonNull StaggeredGridLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.listener = listener;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                    .findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = STARTING_PAGE_INDEX;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the data set count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            listener.onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                // Always grab the first span's last item and set that to begin
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                // Update the size if the position is greater than the previously recorded size
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // Call this method whenever performing new searches
    public void resetState() {
        this.currentPage = STARTING_PAGE_INDEX;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public void setOnLoadMoreListener(@Nullable OnLoadMoreListener listener) {
        this.listener = listener;
    }

}
