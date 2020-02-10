package com.example.bestmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


import androidx.recyclerview.widget.RecyclerView;

import com.example.bestmovies.data.Movie;
import com.example.bestmovies.dataBase.FavoriteMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private Movie [] mMovie;

    Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.main_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.mMovieNameTextView.setText(mMovie[position].getTitle());

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + mMovie[position].getPoster()).into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        if(mMovie == null)
            return 0;
        return mMovie.length;
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        public final ImageView mPosterImageView;
        public final TextView mMovieNameTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieNameTextView = (TextView) itemView.findViewById(R.id.tv_movie_name);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.image_view_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movieDetails = mMovie[adapterPosition];
            mClickHandler.onClick(movieDetails);

        }
    }

    public void setMovieData(Movie [] movieData){
        mMovie = movieData;
        notifyDataSetChanged();
    }
}
