package schaller.com.movetime.movies_list.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;
import schaller.com.movetime.movies_list.models.MoviePosterItem;
import schaller.com.movetime.movies_list.util.MoviePosterDiffUtilCallback;
import schaller.com.movetime.progress_adapter.adapter.ProgressAdapter;

public class MovieListAdapter extends ProgressAdapter<MoviePosterItem> {

    private List<MoviePosterItem> moviePosterItems = new ArrayList<>();
    private OnMovieClickListener movieClickListener;

    public interface OnMovieClickListener {

        void onMovieClick(@NonNull MoviePosterItem moviePosterItem);

    }

    public MovieListAdapter(@NonNull StaggeredGridLayoutManager layoutManager) {
        super(layoutManager);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        switch (viewType) {
            case ItemType.ITEM:
                return new MoviePosterViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.movie_poster_item,
                                parent,
                                false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ItemType.ITEM:
                bindMoviePosterViewHolder((MoviePosterViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public List<MoviePosterItem> getItems() {
        return moviePosterItems;
    }

    @Override
    public int getItemCount() {
        return moviePosterItems.size();
    }

    public void setMovieClickListener(@Nullable OnMovieClickListener movieClickListener) {
        this.movieClickListener = movieClickListener;
    }

    public void setMoviePosterItems(@NonNull List<MoviePosterItem> moviePosterItems) {
        final MoviePosterDiffUtilCallback callback = new MoviePosterDiffUtilCallback(
                this.moviePosterItems, moviePosterItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.moviePosterItems.clear();
        this.moviePosterItems.addAll(moviePosterItems);
        diffResult.dispatchUpdatesTo(this);
    }

    private void bindMoviePosterViewHolder(@NonNull final MoviePosterViewHolder holder,
                                           int position) {
        final MoviePosterItem moviePosterItem = moviePosterItems.get(position);

        holder.posterTitleView.setText(moviePosterItem.getTitle());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                holder.posterTitleView,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        // Cancel previous on-going requests for this view before attempted to load again
        Picasso.with(holder.imageView.getContext()).cancelRequest(holder.imageView);
        Picasso.with(holder.imageView.getContext())
                .load(moviePosterItem.getPosterPath())
                .centerInside()
                .fit()
                .placeholder(R.drawable.poster_image_placeholder)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.posterTitleView.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.posterTitleView.setVisibility(View.VISIBLE);
                    }
                });
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieClickListener == null) {
                    return;
                }
                movieClickListener.onMovieClick(moviePosterItem);
            }
        });
    }

    //region ViewHolders
    class MoviePosterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_card_layout) FrameLayout frameLayout;
        @BindView(R.id.poster_image) ImageView imageView;
        @BindView(R.id.poster_progress_bar) ProgressBar progressBar;
        @BindView(R.id.poster_title) TextView posterTitleView;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            posterTitleView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            posterTitleView.setVisibility(View.GONE);
        }
    }
    //endregion ViewHolders

}
