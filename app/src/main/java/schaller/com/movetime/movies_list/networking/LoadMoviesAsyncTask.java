package schaller.com.movetime.movies_list.networking;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

import javax.net.ssl.HttpsURLConnection;

import schaller.com.movetime.BuildConfig;
import schaller.com.movetime.movies_list.models.MovieSummaryItem;
import schaller.com.movetime.movies_list.models.MovieSummaryResponse;

public class LoadMoviesAsyncTask
        extends AsyncTask<String[], Void, MovieSummaryResponse> {

    public interface OnLoadMovieSummaryCallback {

        void onPreExecuteLoadMovieSummary();

        void onPostExecuteLoadMovieSummary(MovieSummaryResponse movieSummaryResponse);

    }

    private static final String API_TOKEN_KEY = "api_key";
    private static final String API_TOKEN_VALUE = BuildConfig.MOVIE_DB_KEY;
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final String PATH = "api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String DISCOVER_MOVIE_PATH = "discover/movie";
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;

    public static final String PAGE_QUERY_KEY = "page";

    private final OnLoadMovieSummaryCallback callback;

    public LoadMoviesAsyncTask(@Nullable OnLoadMovieSummaryCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (callback == null) {
            return;
        }
        callback.onPreExecuteLoadMovieSummary();
    }

    @Override
    protected MovieSummaryResponse doInBackground(String[]... params) {
        MovieSummaryResponse response;

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme("https")
                .authority(PATH)
                .appendPath(API_VERSION)
                .appendEncodedPath(getApi())
                .appendQueryParameter(API_TOKEN_KEY, API_TOKEN_VALUE);
        for (String[] param : params) {
            verifyParameter(param);
            uriBuilder.appendQueryParameter(param[0], param[1]);
        }
        try {
            URL myUrl = new URL(uriBuilder.build().toString());
            HttpsURLConnection connection = (HttpsURLConnection) myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Connection", "close");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String error = readInputStream(connection.getErrorStream()).toString();
                response = new MovieSummaryResponse(
                        MovieSummaryResponse.ResponseStatus.ERROR,
                        null);
                Log.e(error, "Network error: %s");
            } else {
                StringBuilder stringBuilder = readInputStream(connection.getInputStream());
                response = new MovieSummaryResponse(
                        MovieSummaryResponse.ResponseStatus.SUCCESS,
                        MovieSummaryItem.parseJSON(stringBuilder.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = new MovieSummaryResponse(
                    MovieSummaryResponse.ResponseStatus.ERROR,
                    null);
        }

        return response;
    }

    @Override
    protected void onPostExecute(MovieSummaryResponse movieSummaryResponse) {
        if (callback == null) {
            return;
        }
        callback.onPostExecuteLoadMovieSummary(movieSummaryResponse);
    }

    //region helper functions
    public String getApi() {
        return DISCOVER_MOVIE_PATH;
    }

    private StringBuilder readInputStream(InputStream inputStream) throws IOException {
        String inputLine;
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        //Check if the line we are reading is not null
        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        reader.close();
        streamReader.close();
        return stringBuilder;
    }

    private void verifyParameter(String[] param) {
        // Verify page range
        if (param[0].equals(PAGE_QUERY_KEY) && Integer.valueOf(param[1]) <= 0) {
            throw new InvalidParameterException("Cannot set the page to less than 1");
        }
    }
    //endregion helper functions

}
