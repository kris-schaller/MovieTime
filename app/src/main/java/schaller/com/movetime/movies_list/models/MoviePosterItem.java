package schaller.com.movetime.movies_list.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import static android.support.v4.util.Preconditions.checkStringNotEmpty;

@SuppressWarnings("unused, WeakerAccess")
public class MoviePosterItem implements Parcelable {

    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185//%s";

    @SerializedName("adult")
    private boolean adultContent;

    @SerializedName("backdrop_path")
    private String backgroundPath;

    @SerializedName("id")
    private int id;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDateAsString;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    private MoviePosterItem(@NonNull Builder builder) {
        this.adultContent = builder.adultContent;
        this.backgroundPath = builder.backgroundPath;
        this.id = builder.id;
        this.originalLanguage = builder.originalLanguage;
        this.originalTitle = builder.originalTitle;
        this.overview = builder.overview;
        this.popularity = builder.popularity;
        this.posterPath = builder.posterPath;
        this.releaseDateAsString = builder.releaseDateAsString;
        this.title = builder.title;
        this.video = builder.video;
        this.voteAverage = builder.voteAverage;
        this.voteCount = builder.voteCount;
    }

    public String getBackgroundPath() {
        return String.format(IMAGE_PATH, backgroundPath);
    }

    public int getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return String.format(IMAGE_PATH, posterPath);
    }

    public String getReleaseDateAsString() {
        return releaseDateAsString;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isAdultContent() {
        return adultContent;
    }

    public boolean hasVideo() {
        return video;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static MoviePosterItem parseJSON(String response) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(response, MoviePosterItem.class);
    }

    public void setAdultContent(boolean adultContent) {
        this.adultContent = adultContent;
    }

    public void setBackgroundPath(@NonNull String backgroundPath) {
        this.backgroundPath = String.format(IMAGE_PATH, backgroundPath);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalLanguage(@NonNull String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(@NonNull String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(@NonNull String overview) {
        this.overview = overview;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(@NonNull String posterPath) {
        this.posterPath = String.format(IMAGE_PATH, posterPath);
    }

    public void setReleaseDateAsString(@NonNull String releaseDateAsString) {
        this.releaseDateAsString = releaseDateAsString;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    //region Builder
    public static class Builder {

        private boolean adultContent;
        private String backgroundPath;
        private int id;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private double popularity;
        private String posterPath;
        private String releaseDateAsString;
        private String title;
        private boolean video;
        private double voteAverage;
        private int voteCount;

        public Builder() {
            // no-op
        }

        private Builder(MoviePosterItem moviePosterItem) {
            this.adultContent = moviePosterItem.isAdultContent();
            this.backgroundPath = moviePosterItem.getBackgroundPath();
            this.id = moviePosterItem.getId();
            this.originalLanguage = moviePosterItem.getOriginalLanguage();
            this.originalTitle = moviePosterItem.getOriginalTitle();
            this.overview = moviePosterItem.getOverview();
            this.popularity = moviePosterItem.getPopularity();
            this.posterPath = moviePosterItem.getPosterPath();
            this.releaseDateAsString = moviePosterItem.getReleaseDateAsString();
            this.title = moviePosterItem.getTitle();
            this.video = moviePosterItem.hasVideo();
            this.voteAverage = moviePosterItem.getVoteAverage();
            this.voteCount = moviePosterItem.getVoteCount();
        }

        @SuppressWarnings("RestrictedApi")
        public MoviePosterItem build() {
            checkStringNotEmpty(backgroundPath);
            checkStringNotEmpty(originalLanguage);
            checkStringNotEmpty(originalTitle);
            checkStringNotEmpty(overview);
            checkStringNotEmpty(posterPath);
            checkStringNotEmpty(releaseDateAsString);
            checkStringNotEmpty(title);
            return new MoviePosterItem(this);
        }

        public Builder setAdultContent(boolean adultContent) {
            this.adultContent = adultContent;
            return this;
        }

        public Builder setBackgroundPath(@NonNull String backgroundPath) {
            this.backgroundPath = String.format(IMAGE_PATH, backgroundPath);
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setOriginalLanguage(@NonNull String originalLanguage) {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public Builder setOriginalTitle(@NonNull String originalTitle) {
            this.originalTitle = originalTitle;
            return this;
        }

        public Builder setOverview(@NonNull String overview) {
            this.overview = overview;
            return this;
        }

        public Builder setPopularity(double popularity) {
            this.popularity = popularity;
            return this;
        }

        public Builder setPosterPath(@NonNull String posterPath) {
            this.posterPath = String.format(IMAGE_PATH, posterPath);
            return this;
        }

        public Builder setReleaseDateAsString(@NonNull String releaseDateAsString) {
            this.releaseDateAsString = releaseDateAsString;
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder setVideo(boolean video) {
            this.video = video;
            return this;
        }

        public Builder setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }

    }
    //endregion Builder

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
        return adultContent == that.adultContent
                && id == that.id
                && Double.compare(that.popularity, popularity) == 0
                && video == that.video
                && voteAverage == that.voteAverage
                && voteCount == that.voteCount
                && backgroundPath.equals(that.backgroundPath)
                && originalLanguage.equals(that.originalLanguage)
                && originalTitle.equals(that.originalTitle)
                && overview.equals(that.overview)
                && posterPath.equals(that.posterPath)
                && releaseDateAsString.equals(that.releaseDateAsString)
                && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (adultContent ? 1 : 0);
        result = 31 * result + backgroundPath.hashCode();
        result = 31 * result + id;
        result = 31 * result + originalLanguage.hashCode();
        result = 31 * result + originalTitle.hashCode();
        result = 31 * result + overview.hashCode();
        temp = Double.doubleToLongBits(popularity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + posterPath.hashCode();
        result = 31 * result + releaseDateAsString.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (video ? 1 : 0);
        temp = Double.doubleToLongBits(voteAverage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + voteCount;
        return result;
    }

    @Override
    public String toString() {
        return "MoviePosterItem{" +
                "adultContent=" + adultContent +
                ", backgroundPath='" + backgroundPath + '\'' +
                ", id=" + id +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDateAsString='" + releaseDateAsString + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
    //endregion equals, hashCode, and toString

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adultContent ? (byte) 1 : (byte) 0);
        dest.writeString(this.backgroundPath);
        dest.writeInt(this.id);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDateAsString);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.voteAverage);
        dest.writeInt(this.voteCount);
    }

    private MoviePosterItem(Parcel in) {
        this.adultContent = in.readByte() != 0;
        this.backgroundPath = in.readString();
        this.id = in.readInt();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.releaseDateAsString = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readDouble();
        this.voteCount = in.readInt();
    }

    public static final Creator<MoviePosterItem> CREATOR = new Creator<MoviePosterItem>() {
        @Override
        public MoviePosterItem createFromParcel(Parcel source) {
            return new MoviePosterItem(source);
        }

        @Override
        public MoviePosterItem[] newArray(int size) {
            return new MoviePosterItem[size];
        }
    };
    //endregion Parcelable

}
