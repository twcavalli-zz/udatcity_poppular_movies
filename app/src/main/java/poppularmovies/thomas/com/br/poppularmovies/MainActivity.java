package poppularmovies.thomas.com.br.poppularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URL;
import java.util.ArrayList;
import poppularmovies.thomas.com.br.poppularmovies.data.Movie;
import poppularmovies.thomas.com.br.poppularmovies.utilities.IMDBMovieUtils;
import poppularmovies.thomas.com.br.poppularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static String STATE_INSTANCE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int numberOfColumns = 2;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if(savedInstanceState == null || !savedInstanceState.containsKey(STATE_INSTANCE)) {
            loadMovieData(NetworkUtils.Sort.popular);
        }
        else {
            showMoviesDataView();
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(STATE_INSTANCE);
            mMoviesAdapter.setMovieData(list);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_INSTANCE, mMoviesAdapter.getList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieInfoActivity.class);
        intent.putExtra(MovieInfoActivity.MOVIE_PARAM, movie);
        startActivity(intent);
    }

    private void loadMovieData(NetworkUtils.Sort sort) {
        showMoviesDataView();
        new FetchMoviesTask().execute(sort);
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<NetworkUtils.Sort, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(NetworkUtils.Sort... params) {
            if ( !isOnline() ) {
                return null;
            }

            if (params.length == 0) {
                return null;
            }

            NetworkUtils.Sort sort = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sort);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                ArrayList<Movie> moviesData = IMDBMovieUtils.getMoviesPosters(MainActivity.this, jsonMoviesResponse);
                return moviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mMoviesAdapter.setMovieData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_most_popular:
                loadMovieData(NetworkUtils.Sort.popular);
                setTitle(getString(R.string.menu_most_popular));
                break;
            case R.id.action_highest_rated:
                loadMovieData(NetworkUtils.Sort.top_rated);
                setTitle(getString(R.string.menu_highest_rated));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
