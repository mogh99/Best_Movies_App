package com.example.bestmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bestmovies.data.Movie;
import com.example.bestmovies.dataBase.DataBase;
import com.example.bestmovies.dataBase.FavoriteMovie;
import com.example.bestmovies.dataBase.MainViewModel;
import com.example.bestmovies.utilities.MoviesJsonData;
import com.example.bestmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private DataBase mDataBase;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessage;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBase = DataBase.getInstance(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        onConfigurationChanged(getResources().getConfiguration());

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        //Default Movies to Show at first are Popular Movies
        loadMovieData("popular");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        GridLayoutManager layoutManager = null;

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this, 4);
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 2);
        }

        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void onClickPopular(View v){
        loadMovieData("popular");
    }

    public void onClickTopRated(View v){
        loadMovieData("top_rated");
    }

    public void onClickUpComing(View v){
        loadMovieData("upcoming");
    }

    public void onClickFavorite(View v){
        showRecyclerView();
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getFavListLiveData().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(final List<FavoriteMovie> favoriteMoviesList) {
                if(favoriteMoviesList.size() > 0) {
                    final Movie[] favoriteMovies = new Movie[favoriteMoviesList.size()];
                    FavoriteMovie fMobj = null;
                    for (int i = 0; i < favoriteMoviesList.size(); i++) {
                        fMobj = favoriteMoviesList.get(i);
                        favoriteMovies[i] = new Movie(fMobj.getMovieId(), fMobj.getPoster(), fMobj.getTitle(), fMobj.getOverview(), fMobj.getVoteAverage(), fMobj.getReleaseDate());
                    }
                    mMovieAdapter.setMovieData(favoriteMovies);
                }
                else {
                    mErrorMessage.setText(R.string.main_error_message_favorite);
                    showErrorMessage();
                }
            }
        });
    }

    @Override
    public void onClick(Movie movieDetails) {
        Context context = this;
        Class destinationClass = MovieDetails.class;

        Intent intent = new Intent(context, destinationClass);

        intent.putExtra("id", movieDetails.getId());
        intent.putExtra("poster", movieDetails.getPoster());
        intent.putExtra("title", movieDetails.getTitle());
        intent.putExtra("overview", movieDetails.getOverview());
        intent.putExtra("voteAverage", movieDetails.getVoteAverage());
        intent.putExtra("releaseDate", movieDetails.getReleaseDate());

        startActivity(intent);
    }


    public class FetchMovieData extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected Movie [] doInBackground(String... category) {
            if(category.length == 0)
                return null;

            String requestedCategory = category[0];
            String jsonMovieResponse;
            Movie [] moviesInformation = null;

            //default page is 1 for now
            URL url = NetworkUtils.buildUrl(requestedCategory, 1);

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
            if(moviesInformation != null){
                showRecyclerView();
                mMovieAdapter.setMovieData(moviesInformation);
            }
            else {
                mErrorMessage.setText(R.string.main_error_message);
                showErrorMessage();
            }

        }
    }

    private void showRecyclerView(){
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loadMovieData(String category){
        showRecyclerView();

        new FetchMovieData().execute(category);
    }
}
