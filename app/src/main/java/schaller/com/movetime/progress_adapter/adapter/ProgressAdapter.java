package schaller.com.movetime.progress_adapter.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;

/**
 * Adapter that can be extended to turn a RecyclerView adapter into one with a progress indicator
 * at the bottom.
 * <p>
 * Any class extending this should implement the abstract method {@link #getItems()} which should
 * return the list of items held by the adapter.
 * </p>
 *
 * @param <T> The Object type contained in the adapter list
 */
public abstract class ProgressAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntDef({
            ItemType.PROGRESS_ITEM,
            ItemType.ITEM
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType {

        int PROGRESS_ITEM = 0;
        int ITEM = 1;

    }

    private RecyclerView.LayoutManager layoutManager;
    private boolean loading = false;

    public ProgressAdapter(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public ProgressAdapter(@NonNull final GridLayoutManager layoutManager) {
        // Set the span lookup here so that it only gets called once instead of every time the view
        // is bound
        layoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (getItemViewType(position) == ItemType.PROGRESS_ITEM) {
                            return layoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
        this.layoutManager = layoutManager;
    }

    public ProgressAdapter(@NonNull StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != ItemType.PROGRESS_ITEM) {
            return null;
        }
        return new ProgressBarViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.loading_item,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // If the layout manager is anything besides a StaggeredGridLayoutManager ignore it
        if (!(layoutManager instanceof StaggeredGridLayoutManager)
                && getItemViewType(position) != ItemType.PROGRESS_ITEM) {
            return;
        }
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }

    @Override
    public int getItemViewType(int position) {
        return getItems().get(position) != null ? ItemType.ITEM : ItemType.PROGRESS_ITEM;
    }

    /**
     * Return the list of items stored in the adapter.
     *
     * @return {@link List<T>} where {@link T} is the object type contained in the list
     */
    public abstract List<T> getItems();

    public void showLoadingIndicator(boolean show) {
        // Prevent loading if we're already in a loading state
        if (loading && show) {
            return;
        }
        loading = show;
        int size = getItems().size() - 1;
        if (show) {
            // Adding null to the list indicates during lookup that it should be treated as a
            // progress item
            getItems().add(null);
            notifyItemInserted(size);
            return;
        }
        getItems().remove(size);
        notifyItemRemoved(size);
    }

    //region ViewHolder
    class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_loading_progress_bar) ProgressBar progressBar;

        ProgressBarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
    //endregion ViewHolder

}
