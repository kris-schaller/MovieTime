package schaller.com.movetime.movies_list.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.google.common.base.Optional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Response item containing a {@link ResponseStatus} and a {@link MovieSummaryItem}. Meant to be
 * used as a response for {@link schaller.com.movetime.movies_list.networking.LoadMoviesAsyncTask}.
 */
public class MovieSummaryResponse implements Parcelable {

    @IntDef({
            ResponseStatus.ERROR,
            ResponseStatus.SUCCESS
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponseStatus {

        int ERROR = 0;
        int SUCCESS = 1;

    }

    private final MovieSummaryItem movieSummaryItem;
    private final int responseStatus;

    public MovieSummaryResponse(@ResponseStatus int responseStatus,
                                @Nullable MovieSummaryItem movieSummaryItem) {
        this.responseStatus = responseStatus;
        this.movieSummaryItem = movieSummaryItem;
    }

    public Optional<MovieSummaryItem> getMovieSummaryItem() {
        return Optional.fromNullable(movieSummaryItem);
    }

    @ResponseStatus
    public int getResponseStatus() {
        return responseStatus;
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
        MovieSummaryResponse that = (MovieSummaryResponse) o;
        return responseStatus == that.responseStatus && movieSummaryItem != null
                ? movieSummaryItem.equals(that.movieSummaryItem)
                : that.movieSummaryItem == null;

    }

    @Override
    public int hashCode() {
        int result = movieSummaryItem != null ? movieSummaryItem.hashCode() : 0;
        result = 31 * result + responseStatus;
        return result;
    }
    //endregion equals & hashcode

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.movieSummaryItem, flags);
        dest.writeInt(this.responseStatus);
    }

    protected MovieSummaryResponse(Parcel in) {
        this.movieSummaryItem = in.readParcelable(MovieSummaryItem.class.getClassLoader());
        this.responseStatus = in.readInt();
    }

    public static final Creator<MovieSummaryResponse> CREATOR
            = new Creator<MovieSummaryResponse>() {
        @Override
        public MovieSummaryResponse createFromParcel(Parcel source) {
            return new MovieSummaryResponse(source);
        }

        @Override
        public MovieSummaryResponse[] newArray(int size) {
            return new MovieSummaryResponse[size];
        }
    };
    //endregion Parcelable

}
