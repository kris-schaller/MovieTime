package schaller.com.movetime.movies_list;

import android.os.Bundle;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;
import schaller.com.movetime.movies_list.adapter.MovieListAdapter;
import schaller.com.movetime.movies_list.models.MoviePosterItem;
import schaller.com.movetime.movies_list.util.MovieListUtil;

public class MovieListActivity extends AppCompatActivity
        implements MovieListAdapter.OnMovieClickListener {

    private List<MoviePosterItem> moviePosterItemList = new ArrayList<>();
    private MovieListAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @BindView(R.id.movie_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_layout);
        ButterKnife.bind(this);

        moviePosterItemList.addAll(MovieListUtil.generateMockMovieDataList(10));
        adapter = new MovieListAdapter();
        adapter.setMoviePosterItems(moviePosterItemList);
        adapter.setMovieClickListener(this);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
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
                        return o1.getRating() < o2.getRating() ? -1
                                : o1.getRating() > o2.getRating() ? 1
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
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setLayoutManager(null);
        adapter.setMovieClickListener(null);
    }

    //region OnMovieClickListener
    @Override
    public void onMovieClick(@NonNull MoviePosterItem moviePosterItem) {
        Toast.makeText(this, moviePosterItem.getMovieTitle(), Toast.LENGTH_SHORT).show();
    }
    //endregion OnMovieClickListener
}
