package schaller.com.movetime.movies_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import schaller.com.movetime.R;
import schaller.com.movetime.layout_manager.StaggeredGridAutoFitLayoutManager;
import schaller.com.movetime.movie_details.MovieDetailsActivity;
import schaller.com.movetime.movies_list.adapter.MovieListAdapter;
import schaller.com.movetime.movies_list.models.MoviePosterItem;
import schaller.com.movetime.movies_list.models.MovieSummaryItem;
import schaller.com.movetime.movies_list.models.MovieSummaryResponse;
import schaller.com.movetime.movies_list.networking.LoadMoviesAsyncTask;
import schaller.com.movetime.scroll_listener.EndlessOnScrollListener;
import schaller.com.movetime.view.ProgressErrorView;

public class MovieListActivity extends AppCompatActivity
        implements MovieListAdapter.OnMovieClickListener,
        EndlessOnScrollListener.OnLoadMoreListener {

    private static final String MOVIE_LIST_KEY = "movie_list_key";
    private static final String LAST_LOADED_PAGE_KEY = "last_loaded_page_key";

    // Suppress these warnings so we can make these requests static to avoid memory leaks
    @SuppressWarnings("FieldCanBeLocal")
    private static LoadMoviesAsyncTask loadMovieInitialListRequest;
    @SuppressWarnings("FieldCanBeLocal")
    private static LoadMoviesAsyncTask loadMoviePageRequest;
    @SuppressWarnings("FieldCanBeLocal")
    private static LoadMoviesAsyncTask loadPopularMovieListRequest;
    @SuppressWarnings("FieldCanBeLocal")
    private static LoadMoviesAsyncTask loadTopRatedMovieListRequest;

    private ArrayList<MoviePosterItem> moviePosterItemList = new ArrayList<>();
    private MovieListAdapter adapter;
    private int currentPage = 1;
    private StaggeredGridAutoFitLayoutManager staggeredGridAutoFitLayoutManager;
    private EndlessOnScrollListener endlessOnScrollListener;

    @BindView(R.id.movie_list_error_text) TextView errorText;
    @BindView(R.id.list_retry_button) AppCompatButton retryImage;
    @BindView(R.id.movie_list_progress_bar) ProgressErrorView progressErrorView;
    @BindView(R.id.movie_list) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        staggeredGridAutoFitLayoutManager = new StaggeredGridAutoFitLayoutManager(
                this,
                StaggeredGridLayoutManager.VERTICAL,
                Math.round(getResources().getDimension(R.dimen.list_poster_width)));

        adapter = new MovieListAdapter(staggeredGridAutoFitLayoutManager);
        adapter.setMovieClickListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            moviePosterItemList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            //noinspection ConstantConditions
            adapter.setMoviePosterItemsLoading(moviePosterItemList);
        }

        endlessOnScrollListener = new EndlessOnScrollListener(
                staggeredGridAutoFitLayoutManager,
                this);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAST_LOADED_PAGE_KEY)) {
            currentPage = savedInstanceState.getInt(LAST_LOADED_PAGE_KEY);
        }
        recyclerView.setLayoutManager(staggeredGridAutoFitLayoutManager);
        recyclerView.addOnScrollListener(endlessOnScrollListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!moviePosterItemList.isEmpty()) {
            return;
        }
        loadInitialMovieList();
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
                adapter.setMoviePosterItemsLoading(moviePosterItemList);
                // TODO This is the network request required to complete the project, but it's
                // TODO unnecessary since the value is included in the object response & you lose
                // TODO the animation for sorting.
                // loadPopularMovieList();
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
                adapter.setMoviePosterItemsLoading(moviePosterItemList);
                // TODO This is the network request required to complete the project, but it's
                // TODO unnecessary since the value is included in the object response & you lose
                // TODO the animation for sorting.
                // loadTopRatedMovieList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, moviePosterItemList);
        outState.putInt(LAST_LOADED_PAGE_KEY, currentPage);
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
        startActivity(MovieDetailsActivity.getIntentWithArguments(this, moviePosterItem));
    }
    //endregion OnMovieClickListener

    //region OnLoadMoreListener
    @Override
    public void onLoadMore(int totalItemsCount, RecyclerView view) {
        String[] pageParams = {LoadMoviesAsyncTask.PAGE_QUERY_KEY, String.valueOf(currentPage)};
        loadMoviePageRequest = new LoadMoviesAsyncTask(
                new LoadMoviesAsyncTask.OnLoadMovieSummaryCallback() {
                    @Override
                    public void onPreExecuteLoadMovieSummary() {
                        onMoviePagePreLoad();
                    }

                    @Override
                    public void onPostExecuteLoadMovieSummary(
                            MovieSummaryResponse movieSummaryResponse) {
                        if (movieSummaryResponse.getResponseStatus()
                                == MovieSummaryResponse.ResponseStatus.ERROR) {
                            onMoviePageLoadError();
                            return;
                        }
                        //noinspection OptionalGetWithoutIsPresent
                        onMoviePageLoadSuccess(movieSummaryResponse.getMovieSummaryItem().get());
                    }
                });

        loadMoviePageRequest.execute(pageParams);
    }
    //endregion OnLoadMoreListener

    //region network request helpers
    private void onInitialMovieLoadError() {
        progressErrorView.setVisibility(View.VISIBLE);
        progressErrorView.showError();
        recyclerView.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        retryImage.setVisibility(View.VISIBLE);
    }

    private void onInitialMoviePreLoad() {
        progressErrorView.setVisibility(View.VISIBLE);
        recyclerView.removeOnScrollListener(endlessOnScrollListener);
        recyclerView.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        retryImage.setVisibility(View.GONE);
        progressErrorView.showProgress();
    }

    private void onInitialMovieLoadSuccess(@NonNull MovieSummaryItem movieSummaryItem) {
        moviePosterItemList.addAll(movieSummaryItem.getMoviePosterItems());
        progressErrorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (currentPage >= movieSummaryItem.getTotalPages()) {
            adapter.setMoviePosterItems(moviePosterItemList);
        } else {
            adapter.setMoviePosterItemsLoading(moviePosterItemList);
            currentPage++;
        }
        recyclerView.addOnScrollListener(endlessOnScrollListener);
    }

    private void onMoviePageLoadError() {
        recyclerView.addOnScrollListener(endlessOnScrollListener);
    }

    private void onMoviePagePreLoad() {
        recyclerView.removeOnScrollListener(endlessOnScrollListener);
    }

    private void onMoviePageLoadSuccess(@NonNull MovieSummaryItem movieSummaryItem) {
        moviePosterItemList.addAll(movieSummaryItem.getMoviePosterItems());
        if (currentPage >= movieSummaryItem.getTotalPages()) {
            adapter.setMoviePosterItems(moviePosterItemList);
        } else {
            adapter.setMoviePosterItemsLoading(moviePosterItemList);
            currentPage++;
        }
        recyclerView.addOnScrollListener(endlessOnScrollListener);
    }
    //endregion network request helpers

    private void loadInitialMovieList() {
        loadMovieInitialListRequest = new LoadMoviesAsyncTask(
                new LoadMoviesAsyncTask.OnLoadMovieSummaryCallback() {
                    @Override
                    public void onPreExecuteLoadMovieSummary() {
                        onInitialMoviePreLoad();
                    }

                    @Override
                    public void onPostExecuteLoadMovieSummary(
                            MovieSummaryResponse movieSummaryResponse) {
                        if (movieSummaryResponse.getResponseStatus()
                                == MovieSummaryResponse.ResponseStatus.ERROR) {
                            onInitialMovieLoadError();
                            return;
                        }
                        //noinspection OptionalGetWithoutIsPresent
                        onInitialMovieLoadSuccess(movieSummaryResponse.getMovieSummaryItem().get());
                    }
                });
        loadMovieInitialListRequest.execute();
    }

    private void loadPopularMovieList() {
        String[] pageParams = {LoadMoviesAsyncTask.SORT_BY, LoadMoviesAsyncTask.POPULARITY};
        loadPopularMovieListRequest = new LoadMoviesAsyncTask(
                new LoadMoviesAsyncTask.OnLoadMovieSummaryCallback() {
                    @Override
                    public void onPreExecuteLoadMovieSummary() {
                        onInitialMoviePreLoad();
                    }

                    @Override
                    public void onPostExecuteLoadMovieSummary(
                            MovieSummaryResponse movieSummaryResponse) {
                        if (movieSummaryResponse.getResponseStatus()
                                == MovieSummaryResponse.ResponseStatus.ERROR) {
                            onInitialMovieLoadError();
                            return;
                        }
                        //noinspection OptionalGetWithoutIsPresent
                        onInitialMovieLoadSuccess(movieSummaryResponse.getMovieSummaryItem().get());
                    }
                });
        loadPopularMovieListRequest.execute(pageParams);
    }

    private void loadTopRatedMovieList() {
        String[] pageParams = {LoadMoviesAsyncTask.SORT_BY, LoadMoviesAsyncTask.VOTE_AVG};
        loadTopRatedMovieListRequest = new LoadMoviesAsyncTask(
                new LoadMoviesAsyncTask.OnLoadMovieSummaryCallback() {
                    @Override
                    public void onPreExecuteLoadMovieSummary() {
                        onInitialMoviePreLoad();
                    }

                    @Override
                    public void onPostExecuteLoadMovieSummary(
                            MovieSummaryResponse movieSummaryResponse) {
                        if (movieSummaryResponse.getResponseStatus()
                                == MovieSummaryResponse.ResponseStatus.ERROR) {
                            onInitialMovieLoadError();
                            return;
                        }
                        //noinspection OptionalGetWithoutIsPresent
                        onInitialMovieLoadSuccess(movieSummaryResponse.getMovieSummaryItem().get());
                    }
                });
        loadTopRatedMovieListRequest.execute(pageParams);
    }

    @OnClick(R.id.list_retry_button)
    public void onRetryClick() {
        loadInitialMovieList();
    }

}
