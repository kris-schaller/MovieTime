package schaller.com.movetime.movies_list.networking;


import android.support.annotation.Nullable;

public class LoadTopRatedMoviesAsyncTask extends LoadMoviesAsyncTask {

    private static final String DISCOVER_API = "movie/top_rated";

    public LoadTopRatedMoviesAsyncTask(@Nullable OnLoadMovieSummaryCallback callback) {
        super(callback);
    }

    @Override
    public String getApi() {
        return DISCOVER_API;
    }
}
