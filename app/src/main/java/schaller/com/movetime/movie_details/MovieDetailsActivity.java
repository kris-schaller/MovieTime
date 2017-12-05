package schaller.com.movetime.movie_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import schaller.com.movetime.R;
import schaller.com.movetime.movie_details.arguments.MovieDetailArguments;
import schaller.com.movetime.movies_list.models.MoviePosterItem;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String ARGUMENTS_EXTRA = "ARGUMENTS";

    @BindView(R.id.description) TextView descriptionView;
    @BindView(R.id.poster) ImageView posterView;
    @BindView(R.id.release_date_label) TextView releaseDateLabel;
    @BindView(R.id.release_date) TextView releaseDateView;
    @BindView(R.id.star) ImageView starImageView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.vote_avg) TextView voteAverageView;

    private MovieDetailArguments arguments;

    public static MovieDetailArguments getArguments(@NonNull MoviePosterItem moviePosterItem) {
        return new MovieDetailArguments.Builder()
                .posterUrl(moviePosterItem.getPosterPath())
                .releaseDate(moviePosterItem.getReleaseDateAsString())
                .synopsis(moviePosterItem.getOverview())
                .title(moviePosterItem.getTitle())
                .voteAvg(moviePosterItem.getVoteAverage())
                .build();
    }

    public static Intent getIntentWithArguments(@NonNull Context context,
                                                @NonNull MoviePosterItem moviePosterItem) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENTS_EXTRA, getArguments(moviePosterItem));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            arguments = getIntent().getExtras().getParcelable(ARGUMENTS_EXTRA);
        }
        if (arguments == null) {
            return;
        }
        getSupportActionBar().setTitle(arguments.getTitle());
        descriptionView.setText(arguments.getSynopsis());
        if (!Strings.isNullOrEmpty(arguments.getPosterUrl())) {
            Picasso.with(this).cancelRequest(posterView);
            Picasso.with(this).load(arguments.getPosterUrl()).into(posterView);
        }
        if (arguments.getVoteAvg() == 0.0) {
            voteAverageView.setVisibility(View.GONE);
            starImageView.setVisibility(View.GONE);
        } else {
            voteAverageView.setText(String.valueOf(arguments.getVoteAvg()));
        }
        if (arguments.getReleaseDate() == null) {
            releaseDateLabel.setVisibility(View.GONE);
            releaseDateView.setVisibility(View.GONE);
        } else {
            releaseDateView.setText(arguments.getReleaseDate());
        }
    }
}
