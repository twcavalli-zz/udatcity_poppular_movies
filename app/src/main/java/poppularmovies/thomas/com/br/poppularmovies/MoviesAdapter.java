package poppularmovies.thomas.com.br.poppularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import poppularmovies.thomas.com.br.poppularmovies.data.Movie;

/**
 * Created by Thomas on 12/08/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movie> mMoviesData;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePoster;
        public final TextView mMovieTitle;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
            mMovieTitle = (TextView) view.findViewById(R.id.tvMovieTitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie= mMoviesData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movie = mMoviesData.get(position);

        Picasso.with(movieAdapterViewHolder.mMoviePoster.getContext())
                .load(movie.getPoster(Movie.PosterSize.w185))
                .into(movieAdapterViewHolder.mMoviePoster);

        movieAdapterViewHolder.mMovieTitle.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.size();
    }

    public ArrayList<Movie> getList() {
        return mMoviesData;
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMoviesData = movieData;
        notifyDataSetChanged();
    }
}
