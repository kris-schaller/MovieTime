package schaller.com.movetime.movies_list.models;

import android.support.annotation.NonNull;

import static android.support.v4.util.Preconditions.checkStringNotEmpty;

public class MoviePosterItem {

    private final Builder moviePosterBuilder;

    private MoviePosterItem(@NonNull Builder moviePosterBuilder) {
        this.moviePosterBuilder = moviePosterBuilder;
    }

    public String getMovieId() {
        return moviePosterBuilder.id;
    }

    public String getMovieTitle() {
        return moviePosterBuilder.movieTitle;
    }

    public float getPopularity() {
        return moviePosterBuilder.popularity;
    }

    public String getPosterUrl() {
        return moviePosterBuilder.moviePosterUrl;
    }

    public float getRating() {
        return moviePosterBuilder.rating;
    }

    //region equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoviePosterItem that = (MoviePosterItem) o;
        return moviePosterBuilder.equals(that.moviePosterBuilder);
    }

    @Override
    public int hashCode() {
        return moviePosterBuilder.hashCode();
    }

    @Override
    public String toString() {
        return "MoviePosterItem{"
                + "id=" + moviePosterBuilder.id + ", "
                + "moviePosterUrl=" + moviePosterBuilder.moviePosterUrl + ", "
                + "popularity=" + moviePosterBuilder.popularity + ", "
                + "rating=" + moviePosterBuilder.rating + ", "
                + "title=" + moviePosterBuilder.movieTitle
                + '}';
    }
    //endregion equals, hashCode, and toString

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private String id;
        private String moviePosterUrl;
        private String movieTitle;
        private float popularity;
        private float rating;

        public Builder() {
            // no-op
        }

        private Builder(MoviePosterItem moviePosterItem) {
            this.id = moviePosterItem.getMovieId();
            this.movieTitle = moviePosterItem.getMovieTitle();
            this.moviePosterUrl = moviePosterItem.getPosterUrl();
            this.popularity = moviePosterItem.getPopularity();
            this.rating = moviePosterItem.getRating();
        }

        public Builder setMovieId(@NonNull String movieId) {
            this.id = movieId;
            return this;
        }

        public Builder setMoviePosterUrl(@NonNull String moviePosterUrl) {
            this.moviePosterUrl = moviePosterUrl;
            return this;
        }

        public Builder setMovieTitle(@NonNull String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder setPopularity(float popularity) {
            this.popularity = popularity;
            return this;
        }

        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        @SuppressWarnings("RestrictedApi")
        public MoviePosterItem build() {
            checkStringNotEmpty(id);
            checkStringNotEmpty(movieTitle);
            checkStringNotEmpty(moviePosterUrl);
            return new MoviePosterItem(this);
        }

        //region equals & hashcode
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Builder builder = (Builder) o;
            if (Float.compare(builder.popularity, popularity) != 0) {
                return false;
            }
            if (Float.compare(builder.rating, rating) != 0) {
                return false;
            }
            if (!id.equals(builder.id)) {
                return false;
            }
            if (!moviePosterUrl.equals(builder.moviePosterUrl)) {
                return false;
            }
            return movieTitle.equals(builder.movieTitle);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + moviePosterUrl.hashCode();
            result = 31 * result + movieTitle.hashCode();
            result = 31 * result + (popularity != +0.0f ? Float.floatToIntBits(popularity) : 0);
            result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
            return result;
        }
        //endregion equals & hashcode
    }

}
