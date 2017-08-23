package schaller.com.movetime.movies_list.util;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

import schaller.com.movetime.movies_list.models.MoviePosterItem;

public class MovieListUtil {

    private static final String MOCK_POSTER_URL
            = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
    private static final String MOCK_POSTER_URL_2
            = "http://image.tmdb.org/t/p/w185/iD1U2efUvTr6uPMcSeknfR27ZXM.jpg";
    private static final String MOCK_POSTER_URL_3
            = "http://image.tmdb.org/t/p/w185/9Hj2bqi955SvTa5zj7uZs6sic29.jpg";
    private static final String MOCK_POSTER_URL_4
            = "http://image.tmdb.org/t/p/w185/kOLX6JCD0a0CYhha8uwrsPo12Rb.jpg";
    private static final String MOCK_MOVIE_ID = "mock_id";
    private static final String MOCK_MOVIE_TITLE = "Inception";
    private static final String MOCK_MOVIE_TITLE_2 = "IT";
    private static final String MOCK_MOVIE_TITLE_3 = "The Grand Budapest Hotel";
    private static final String MOCK_MOVIE_TITLE_4
            = "The Jinx: The Life and Deaths of Robert Durst Season 1 Chapter 6: What the Hell Did I Do?";

    //region Generate mock UI models
    public static List<MoviePosterItem> generateMockMovieDataList(
            @IntRange(from = 1, to = 20) int size) {

        List<MoviePosterItem> moviePosterItemList = new ArrayList<>();
        for (int i = 1; i <= size && i <= 20; i++) {
            moviePosterItemList.add(generateMockMovieData(i));
        }
        return moviePosterItemList;
    }
    //endregion Generate mock UI models


    //region helper functions
    private static MoviePosterItem generateMockMovieData(int option) {
        MoviePosterItem item = new MoviePosterItem.Builder()
                .setMoviePosterUrl(MOCK_POSTER_URL)
                .setMovieId(MOCK_MOVIE_ID)
                .setMovieTitle(MOCK_MOVIE_TITLE)
                .setPopularity((float) Math.random())
                .setRating((float) Math.random())
                .build();

        switch (option) {
            case 2:
            case 6:
            case 10:
            case 14:
            case 18:
                return item.newBuilder()
                        .setMovieTitle(MOCK_MOVIE_TITLE_2)
                        .setMoviePosterUrl(MOCK_POSTER_URL_2)
                        .build();
            case 3:
            case 7:
            case 11:
            case 15:
            case 19:
                return item.newBuilder()
                        .setMovieTitle(MOCK_MOVIE_TITLE_3)
                        .setMoviePosterUrl(MOCK_POSTER_URL_3)
                        .build();
            case 4:
            case 8:
            case 12:
            case 16:
            case 20:
                return item.newBuilder()
                        .setMovieTitle(MOCK_MOVIE_TITLE_4)
                        .setMoviePosterUrl(MOCK_POSTER_URL_4)
                        .build();
            case 1:
            case 5:
            case 9:
            case 13:
            case 17:
            default:
                return item;
        }
    }
    //endregion helper functions

}
