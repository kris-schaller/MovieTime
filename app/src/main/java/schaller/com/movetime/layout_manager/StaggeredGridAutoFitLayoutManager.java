package schaller.com.movetime.layout_manager;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;

/**
 * A StaggeredGridLayoutManager that dynamically determine the amount of columns that can fit
 * inside
 * the device's screen based on the {@link #columnWidth} provided. If {@link #columnWidth} is less
 * than or equal to 0 the device value, {@link #MIN_COLUMN_WIDTH}, will be used.
 *
 * The width can be updated by calling {@link #setColumnWidth(int)} which will redraw the list.
 */
public class StaggeredGridAutoFitLayoutManager extends StaggeredGridLayoutManager {

    private static final int MIN_COLUMN_WIDTH = 48;

    private int columnWidth;
    private boolean columnWidthChanged;

    public StaggeredGridAutoFitLayoutManager(@NonNull Context context,
                                             int orientation,
                                             int columnWidth) {
        super(1, orientation);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (columnWidthChanged && columnWidth > 0 && getWidth() > 0 && getHeight() > 0) {
            final int spans = (int) Math.floor(getWidth() / columnWidth);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setSpanCount(spans);
                }
            });
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }

    /**
     * Set the column width that should be used. Based on this value the number of columns will be
     * calculated so that it will hold as many as can fit on screen.
     *
     * @param newColumnWidth the width of the column(s). If {@link #columnWidth} is less than or
     *                       equal to 0 the device value, {@link #MIN_COLUMN_WIDTH}, will be used.
     */
    public void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth <= 0 || newColumnWidth == columnWidth) {
            return;
        }
        columnWidth = newColumnWidth;
        columnWidthChanged = true;
        invalidateSpanAssignments();
    }

    //region helper functions
    private int checkedColumnWidth(@NonNull Context context, int columnWidth) {
        if (columnWidth <= 0) {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    MIN_COLUMN_WIDTH,
                    context.getResources().getDisplayMetrics());
        }
        return columnWidth;
    }
    //endregion helper functions
}
