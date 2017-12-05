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

    private final RecyclerView.LayoutManager mLayoutManager;

    // Threshold based on number of spans
    private int visibleThreshold = VISIBLE_THRESHOLD;
    private OnLoadMoreListener listener;

    public interface OnLoadMoreListener {

        void onLoadMore(int totalItemsCount, RecyclerView view);

    }

    public EndlessOnScrollListener(@NonNull LinearLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        this.listener = listener;
    }

    public EndlessOnScrollListener(@NonNull GridLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.listener = listener;
    }

    public EndlessOnScrollListener(@NonNull StaggeredGridLayoutManager layoutManager,
                                   @Nullable OnLoadMoreListener listener) {
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.listener = listener;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        // If we're current loading or not advancing the list ignore any events
        if (dx <= 0 && dy <= 0) {
            return;
        }

        int lastVisibleItemPosition = 0;
        visibleThreshold = VISIBLE_THRESHOLD;
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

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if ((lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            listener.onLoadMore(totalItemCount, view);
        }
    }

    public void setOnLoadMoreListener(@Nullable OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    //region helper function
    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
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
        // Because we're grabbing the position, but comparing it to size we need to add an offset of
        // 1 because of the 0 starting index
        return maxSize + 1;
    }
    //endregion helper functions

}
