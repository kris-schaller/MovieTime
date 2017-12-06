package schaller.com.movetime.movies_list.networking;

import android.support.annotation.Nullable;

public class LoadPopularMoviesAsyncTask extends LoadMoviesAsyncTask {

    private static final String POPULAR_API = "movie/popular";

    public LoadPopularMoviesAsyncTask(@Nullable OnLoadMovieSummaryCallback callback) {
        super(callback);
    }

    @Override
    public String getApi() {
        return POPULAR_API;
    }

}
