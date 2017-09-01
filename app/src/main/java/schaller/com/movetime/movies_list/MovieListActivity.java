package schaller.com.movetime.movies_list;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;
import schaller.com.movetime.layout_manager.StaggeredGridAutoFitLayoutManager;
import schaller.com.movetime.movies_list.adapter.MovieListAdapter;
import schaller.com.movetime.movies_list.models.MoviePosterItem;
import schaller.com.movetime.movies_list.util.MovieListUtil;
import schaller.com.movetime.scroll_listener.EndlessOnScrollListener;

public class MovieListActivity extends AppCompatActivity
        implements MovieListAdapter.OnMovieClickListener,
        EndlessOnScrollListener.OnLoadMoreListener {

    private static final String MOVIE_LIST_KEY = "movie_list_key";

    private ArrayList<MoviePosterItem> moviePosterItemList = new ArrayList<>();
    private MovieListAdapter adapter;
    private StaggeredGridAutoFitLayoutManager staggeredGridAutoFitLayoutManager;
    private EndlessOnScrollListener endlessOnScrollListener;
    private Handler handler = new Handler();

    @BindView(R.id.movie_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_layout);
        ButterKnife.bind(this);

        staggeredGridAutoFitLayoutManager = new StaggeredGridAutoFitLayoutManager(
                this,
                StaggeredGridLayoutManager.VERTICAL,
                Math.round(getResources().getDimension(R.dimen.list_poster_width)));

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            //noinspection unchecked
            moviePosterItemList = (ArrayList<MoviePosterItem>) savedInstanceState
                    .get(MOVIE_LIST_KEY);
        } else {
            moviePosterItemList.addAll(MovieListUtil.generateMockMovieDataList(10));
        }
        adapter = new MovieListAdapter(staggeredGridAutoFitLayoutManager);
        adapter.setMoviePosterItems(moviePosterItemList);
        adapter.setMovieClickListener(this);

        endlessOnScrollListener = new EndlessOnScrollListener(
                staggeredGridAutoFitLayoutManager,
                this);

        recyclerView.setLayoutManager(staggeredGridAutoFitLayoutManager);
        recyclerView.addOnScrollListener(endlessOnScrollListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity_action:
                Collections.sort(moviePosterItemList, new Comparator<MoviePosterItem>() {
                    @Override
                    public int compare(MoviePosterItem o1, MoviePosterItem o2) {
                        return o1.getPopularity() < o2.getPopularity() ? -1
                                : o1.getPopularity() > o2.getPopularity() ? 1
                                        : 0;
                    }
                });
                adapter.setMoviePosterItems(moviePosterItemList);
                return true;
            case R.id.sort_by_rating_action:
                Collections.sort(moviePosterItemList, new Comparator<MoviePosterItem>() {
                    @Override
                    public int compare(MoviePosterItem o1, MoviePosterItem o2) {
                        return o1.getVoteAverage() < o2.getVoteAverage() ? -1
                                : o1.getVoteAverage() > o2.getVoteAverage() ? 1
                                        : 0;
                    }
                });
                adapter.setMoviePosterItems(moviePosterItemList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, moviePosterItemList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setLayoutManager(null);
        adapter.setMovieClickListener(null);
        endlessOnScrollListener.setOnLoadMoreListener(null);
    }

    //region OnMovieClickListener
    @Override
    public void onMovieClick(@NonNull MoviePosterItem moviePosterItem) {
        Toast.makeText(this, moviePosterItem.getTitle(), Toast.LENGTH_SHORT).show();
    }
    //endregion OnMovieClickListener

    //region OnLoadMoreListener
    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        // These need to be posted on the same thread so we avoid race conditions causing the
        // loading indicator/items to not display
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.showLoadingIndicator(true);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moviePosterItemList.addAll(MovieListUtil.generateMockMovieDataList(10));
                adapter.setMoviePosterItems(moviePosterItemList);
                adapter.showLoadingIndicator(false);
            }
        }, 5000);
    }
    //endregion OnLoadMoreListener

}
