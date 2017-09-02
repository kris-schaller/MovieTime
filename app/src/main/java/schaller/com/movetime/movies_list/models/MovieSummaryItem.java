package schaller.com.movetime.movies_list.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class MovieSummaryItem implements Parcelable {

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int numberOfItems;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<MoviePosterItem> moviePosterItems;

    public MovieSummaryItem(int page,
                            int numberOfItems,
                            int totalPages,
                            @NonNull List<MoviePosterItem> moviePosterItems) {
        this.page = page;
        this.numberOfItems = numberOfItems;
        this.totalPages = totalPages;
        this.moviePosterItems = moviePosterItems;
    }

    public int getPage() {
        return page;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MoviePosterItem> getMoviePosterItems() {
        return moviePosterItems;
    }

    public static MovieSummaryItem parseJSON(String response) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.fromJson(response, MovieSummaryItem.class);
    }

    //region equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieSummaryItem that = (MovieSummaryItem) o;
        return page == that.page
                && numberOfItems == that.numberOfItems
                && totalPages == that.totalPages
                && moviePosterItems.equals(that.moviePosterItems);
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + numberOfItems;
        result = 31 * result + totalPages;
        result = 31 * result + moviePosterItems.hashCode();
        return result;
    }
    //endregion equals and hashcode

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.numberOfItems);
        dest.writeInt(this.totalPages);
        dest.writeTypedList(this.moviePosterItems);
    }

    private MovieSummaryItem(Parcel in) {
        this.page = in.readInt();
        this.numberOfItems = in.readInt();
        this.totalPages = in.readInt();
        this.moviePosterItems = in.createTypedArrayList(MoviePosterItem.CREATOR);
    }

    public static final Creator<MovieSummaryItem> CREATOR = new Creator<MovieSummaryItem>() {
        @Override
        public MovieSummaryItem createFromParcel(Parcel source) {
            return new MovieSummaryItem(source);
        }

        @Override
        public MovieSummaryItem[] newArray(int size) {
            return new MovieSummaryItem[size];
        }
    };
    //endregion Parcelable

}
