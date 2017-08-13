package poppularmovies.thomas.com.br.poppularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import poppularmovies.thomas.com.br.poppularmovies.data.Movie;

public class MovieInfoActivity extends AppCompatActivity {

    public static String MOVIE_PARAM = "movie";
    private Movie mMovie;
    private ImageView mMoviePoster;
    private TextView mMovieRating;
    private TextView mMovieSynopsis;
    private TextView mMovieRealiaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mMovie = null;

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(MOVIE_PARAM)) {
            mMovie = intentThatStartedThisActivity.getParcelableExtra(MOVIE_PARAM);
        }

        mMoviePoster = (ImageView) findViewById(R.id.iv_details_movie_poster);
        mMovieRating = (TextView) findViewById(R.id.tv_details_movie_rating);
        mMovieSynopsis = (TextView) findViewById(R.id.tv_details_movie_synopsis);
        mMovieRealiaseDate = (TextView) findViewById(R.id.tv_details_movie_realease_date);

        setTitle(mMovie.getTitle());

        Picasso.with(this)
                .load(mMovie.getPoster(Movie.PosterSize.w500))
                .into(mMoviePoster);

        mMovieSynopsis.setText(mMovie.getSynopsis());
        mMovieRating.setText(getString(R.string.rating) + mMovie.getRating()+"");
        mMovieRealiaseDate.setText(getString(R.string.release_date) + mMovie.getRelease_date());
    }
}
