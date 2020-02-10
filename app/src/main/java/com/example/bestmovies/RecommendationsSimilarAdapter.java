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
import com.squareup.picasso.Picasso;

public class RecommendationsSimilarAdapter extends RecyclerView.Adapter<RecommendationsSimilarAdapter.RecomSimilarViewHolder>{

    Context context;

    private Movie[] eRecomSimilarMovies;

    private final RecomSimilarOnClickHandler dClickHandler;

    public interface RecomSimilarOnClickHandler{
        void onClick(Movie movie);
    }


    @Override
    public RecomSimilarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.recommendations_similar_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new RecomSimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecomSimilarViewHolder holder, int position) {
        holder.eMovieNameTextView.setText(eRecomSimilarMovies[position].getTitle());

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + eRecomSimilarMovies[position].getPoster()).into(holder.ePosterImageView);

    }

    @Override
    public int getItemCount() {
        if(eRecomSimilarMovies == null)
            return 0;

        return eRecomSimilarMovies.length;
    }

    public RecommendationsSimilarAdapter(RecomSimilarOnClickHandler clickHandler){
        dClickHandler = clickHandler;
    }

    public class RecomSimilarViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        public final ImageView ePosterImageView;
        public final TextView eMovieNameTextView;

        public RecomSimilarViewHolder(View itemView) {
            super(itemView);
            eMovieNameTextView = (TextView) itemView.findViewById(R.id.e_tv_movie_name);
            ePosterImageView = (ImageView) itemView.findViewById(R.id.e_image_view_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movieDetails = eRecomSimilarMovies[adapterPosition];
            dClickHandler.onClick(movieDetails);
        }
    }

    public void setdRecomSimilarMoviesData(Movie [] movieData){
        eRecomSimilarMovies = movieData;
        notifyDataSetChanged();
    }
}
