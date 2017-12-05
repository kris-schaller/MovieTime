package schaller.com.movetime.movie_details.arguments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieDetailArguments implements Parcelable {

    private final String posterUrl;
    private final String releaseDate;
    private final String synopsis;
    private final String title;
    private final double voteAvg;

    private MovieDetailArguments(@NonNull Builder builder) {
        this.posterUrl = builder.posterUrl;
        this.releaseDate = builder.releaseDate;
        this.synopsis = builder.synopsis;
        this.title = builder.title;
        this.voteAvg = builder.voteAvg;
    }

    @NonNull
    public String getPosterUrl() {
        return posterUrl;
    }

    @Nullable
    public String getReleaseDate() {
        return releaseDate;
    }

    @NonNull
    public String getSynopsis() {
        return synopsis;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    //region Builder
    public static class Builder {

        private String posterUrl;
        private String releaseDate;
        private String synopsis;
        private String title;
        private double voteAvg;

        public Builder() {
            // no-op
        }

        public Builder(@NonNull MovieDetailArguments movieDetailArguments) {
            this.posterUrl = movieDetailArguments.getPosterUrl();
            this.releaseDate = movieDetailArguments.getReleaseDate();
            this.synopsis = movieDetailArguments.getSynopsis();
            this.title = movieDetailArguments.getTitle();
            this.voteAvg = movieDetailArguments.getVoteAvg();
        }

        public Builder posterUrl(@NonNull String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public Builder releaseDate(@Nullable String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder synopsis(@NonNull String synopsis) {
            this.synopsis = synopsis;
            return this;
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder voteAvg(double voteAvg) {
            this.voteAvg = voteAvg;
            return this;
        }

        public MovieDetailArguments build() {
            return new MovieDetailArguments(this);
        }

    }
    //endregion Builder

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterUrl);
        dest.writeString(this.releaseDate);
        dest.writeString(this.synopsis);
        dest.writeString(this.title);
        dest.writeDouble(this.voteAvg);
    }

    private MovieDetailArguments(Parcel in) {
        this.posterUrl = in.readString();
        this.releaseDate = in.readString();
        this.synopsis = in.readString();
        this.title = in.readString();
        this.voteAvg = in.readDouble();
    }

    public static final Creator<MovieDetailArguments> CREATOR = new Creator<MovieDetailArguments>() {
        @Override
        public MovieDetailArguments createFromParcel(Parcel source) {
            return new MovieDetailArguments(source);
        }

        @Override
        public MovieDetailArguments[] newArray(int size) {
            return new MovieDetailArguments[size];
        }
    };
    //endregion Parcelable
}
