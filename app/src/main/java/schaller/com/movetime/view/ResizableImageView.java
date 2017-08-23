package schaller.com.movetime.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * ImageView that will scale the view's height or width to fit the image without modifying the
 * image.
 * This acts the same as {@link #setAdjustViewBounds(boolean)}, but is backwards compatible.
 */
public class ResizableImageView extends AppCompatImageView {

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();

        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
                // We know that height should scale
                int scaledHeight = (int) Math
                        .ceil((float) width * ((float) d.getIntrinsicHeight()) / (float) d
                                .getIntrinsicWidth());
                setMeasuredDimension(width, scaledHeight);
            } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
                int scaledWidth = (int) Math
                        .ceil((float) height * ((float) d.getIntrinsicWidth()) / (float) d
                                .getIntrinsicHeight());
                setMeasuredDimension(scaledWidth, height);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
