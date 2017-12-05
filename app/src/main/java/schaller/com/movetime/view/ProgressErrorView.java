package schaller.com.movetime.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;

public class ProgressErrorView extends FrameLayout {

    @BindView(R.id.progress_error_image) ImageView errorView;
    @BindView(R.id.progress_error_progress_bar) ProgressBar progressBar;

    private final AnimatorSet crossFadeAnimationSet = new AnimatorSet();

    public ProgressErrorView(Context context) {
        super(context);
        init();
    }

    public ProgressErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressErrorView(Context context, AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.progress_error_view_layout, this);
        ButterKnife.bind(this);
    }

    public void showError() {
        crossFadeViews(progressBar, errorView);
    }

    public void showProgress() {
        crossFadeViews(errorView, progressBar);
    }

    private void crossFadeViews(final View fadeOutView, final View fadeInView) {
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(fadeInView, "alpha", 0.0f, 1.0f);
        fadeInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fadeInView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // no-op
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // no-op
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // no-op
            }
        });
        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(fadeOutView, "alpha", 1.0f, 0.0f);
        crossFadeAnimationSet.playTogether(fadeOutAnimator, fadeInAnimator);
        crossFadeAnimationSet.setDuration(500);
        crossFadeAnimationSet.start();
    }

}
