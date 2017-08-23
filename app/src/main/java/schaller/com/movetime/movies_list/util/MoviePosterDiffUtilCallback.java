package schaller.com.movetime.movies_list.util;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

import schaller.com.movetime.movies_list.models.MoviePosterItem;

public class MoviePosterDiffUtilCallback extends DiffUtil.Callback {

    private final List<MoviePosterItem> oldMoviePosterItemList;
    private final List<MoviePosterItem> newMoviePosterItemList;

    public MoviePosterDiffUtilCallback(@NonNull List<MoviePosterItem> oldMoviePosterItemList,
                                       @NonNull List<MoviePosterItem> newMoviePosterItemList) {
        this.oldMoviePosterItemList = oldMoviePosterItemList;
        this.newMoviePosterItemList = newMoviePosterItemList;
    }

    @Override
    public int getOldListSize() {
        return oldMoviePosterItemList.size();
    }

    @Override
    public int getNewListSize() {
        return newMoviePosterItemList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMoviePosterItemList.get(oldItemPosition)
                == newMoviePosterItemList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMoviePosterItemList.get(oldItemPosition)
                .equals(newMoviePosterItemList.get(newItemPosition));
    }

}
