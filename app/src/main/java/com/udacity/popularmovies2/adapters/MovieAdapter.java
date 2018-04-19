package com.udacity.popularmovies2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.activities.MovieDetailsActivity;
import com.udacity.popularmovies2.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Activity context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder viewHolder, int position) {
        final Movie movie = mMovies.get(position);

        ImageView imageViewPoster = viewHolder.imageViewPoster;

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + movie.posterPath;
        Picasso.get().load(posterUrl).into(imageViewPoster);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPoster;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewPoster = (ImageView) itemView.findViewById(R.id.imageView_poster);
        }
    }

}
