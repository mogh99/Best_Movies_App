package com.example.bestmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bestmovies.data.Movie;
import com.example.bestmovies.utilities.MoviesJsonData;
import com.example.bestmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class RecommendationsSimilarMovies extends AppCompatActivity implements RecommendationsSimilarAdapter.RecomSimilarOnClickHandler{

    private RecyclerView eRecyclerView;
    private RecommendationsSimilarAdapter eRecomSimilarAdapter;

    private TextView eErrorMessage;

    private ProgressBar eProgressBar;

    private Movie movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_similar_movies);

        eRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        eErrorMessage = (TextView) findViewById(R.id.tv_error_message);

        eProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        eRecyclerView.setLayoutManager(layoutManager);
        eRecyclerView.setHasFixedSize(true);

        eRecomSimilarAdapter = new RecommendationsSimilarAdapter(this);

        eRecyclerView.setAdapter(eRecomSimilarAdapter);

        loadExtras(getIntent());
        loadMovieData();
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetails.class;

        Intent intent = new Intent(context, destinationClass);

        intent.putExtra("id", movie.getId());
        intent.putExtra("poster", movie.getPoster());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("voteAverage", movie.getVoteAverage());
        intent.putExtra("releaseDate", movie.getReleaseDate());

        startActivity(intent);
    }

    public class FetchRecomSimilarMovies extends AsyncTask<Movie, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected Movie[] doInBackground(Movie... movieDetails) {
            if(movieDetails.length == 0)
                return null;

            Movie movieDetailsElement = movieDetails[0];
            String jsonMovieResponse;
            Movie [] moviesInformation = null;

            //default page is 1 for now
            URL url = NetworkUtils.buildUrl(movieDetailsElement.getId(), movieDetailsElement.getMovieSubInformations());

            try{
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                moviesInformation = MoviesJsonData.getInformationFromJsonString(jsonMovieResponse);

            }catch(IOException e){
                e.printStackTrace();
            }

            return moviesInformation;
        }

        @Override
        protected void onPostExecute(Movie[] moviesInformation) {
            if(moviesInformation != null) {
                showRecyclerView();
                eRecomSimilarAdapter.setdRecomSimilarMoviesData(moviesInformation);
            }
            else
                showErrorMessage();
        }
    }



    private void showRecyclerView(){
        eProgressBar.setVisibility(View.INVISIBLE);
        eErrorMessage.setVisibility(View.INVISIBLE);
        eRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){
        eRecyclerView.setVisibility(View.INVISIBLE);
        eErrorMessage.setVisibility(View.INVISIBLE);
        eProgressBar.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        eRecyclerView.setVisibility(View.INVISIBLE);
        eProgressBar.setVisibility(View.INVISIBLE);
        eErrorMessage.setVisibility(View.VISIBLE);
    }

    public void loadExtras(Intent intent){
        int movieId = 0;
        String movieSubInformations = null;

        if(intent != null){
            if(intent.hasExtra("movieId"))
                movieId = intent.getIntExtra("movieId", 0);
            if(intent.hasExtra("movieSubInformations"))
                movieSubInformations = intent.getStringExtra("movieSubInformations");
        }

        movieDetails = new Movie(movieId, movieSubInformations);
    }

    public void loadMovieData(){
        showRecyclerView();

        new FetchRecomSimilarMovies().execute(movieDetails);
    }
}
