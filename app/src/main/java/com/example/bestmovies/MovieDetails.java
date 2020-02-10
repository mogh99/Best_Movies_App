package com.example.bestmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bestmovies.data.Movie;
import com.example.bestmovies.dataBase.AppExecutors;
import com.example.bestmovies.dataBase.DataBase;
import com.example.bestmovies.dataBase.FavoriteMovie;
import com.example.bestmovies.utilities.MoviesJsonData;
import com.example.bestmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private DataBase dDataBase;

    static final String ID = "id";
    static final String POSTER = "poster";
    static final String TITLE = "title";
    static final String OVERVIEW = "overview";
    static final String VOTEAVERAGE = "voteAverage";
    static final String RELEASEDATE = "releaseDate";

    static final String REVIEWS = "reviews";
    static final String TRAILERS = "videos";

    private TextView dTitleTextView;
    private ImageView dPosterImageView;
    private TextView dOverviewTextView;
    private TextView dUserRatingTextView;
    private TextView dReleaseDateTexView;

    private Switch dSwitch;

    private Spinner dTrailersSpinner;
    private TextView dTrailersTextView;
    private ProgressBar dTrailersPB;
    private ArrayAdapter dAdapterTrailers;

    private Spinner dReviewsSpinner;
    private TextView dReviewsTextView;
    private ProgressBar dReviewsPB;
    private ArrayAdapter dAdapterReviews;

    private String[] trailers_keys;
    private String[] reviewsText;

    private int count = 0;

    private int id;
    private String poster;
    private String movieLabel;
    private String overView;
    private int voteAverage;
    private String releaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        dDataBase = DataBase.getInstance(getApplicationContext());

        dTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        dPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        dOverviewTextView = (TextView) findViewById(R.id.tv_over_view);
        dUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        dReleaseDateTexView = (TextView) findViewById(R.id.tv_release_date);

        dReviewsSpinner = (Spinner) findViewById(R.id.spinner_reviews);
        dReviewsSpinner.setOnItemSelectedListener(this);
        dReviewsTextView = (TextView) findViewById(R.id.tv_error_message_reviews);
        dReviewsPB = (ProgressBar) findViewById(R.id.pb_loading_indicator_reviews);

        dTrailersSpinner = (Spinner) findViewById(R.id.spinner_trailers);
        dTrailersSpinner.setOnItemSelectedListener(this);
        dTrailersTextView = (TextView) findViewById(R.id.tv_error_message_trailers);
        dTrailersPB = (ProgressBar) findViewById(R.id.pb_loading_indicator_trailers);

        updateViews(getIntent());

        dSwitch = (Switch) findViewById(R.id.switch_favorite);
        checkIfFavorite(this);
        dSwitch.setOnCheckedChangeListener(this);

        loadReviewsTrailers();
    }

    public class FetchReviews extends AsyncTask<Movie, Void, String[]>{

        @Override
        protected void onPreExecute() {
            showProgressBar(REVIEWS);
        }

        @Override
        protected String[] doInBackground(Movie... movieDetails) {
            if(movieDetails.length == 0)
                return null;

            Movie movieDetailsElement = movieDetails[0];
            String jsonMovieResponse;
            String [] reviews = null;

            URL url = NetworkUtils.buildUrl(movieDetailsElement.getId(), movieDetailsElement.getMovieSubInformations());

            try{
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                reviews = MoviesJsonData.getReviewsTrailersInformation(jsonMovieResponse, "content");

            }catch(IOException e){
                e.printStackTrace();
            }

            return reviews;
        }

        @Override
        protected void onPostExecute(String[] reviews) {
            if(reviews != null){
                showSpinner(REVIEWS);
                setSpinnerAdapters(reviews.length, REVIEWS);
                reviewsText = reviews;
            }
            else
                showErrorMessage(REVIEWS);
        }
    }

    public class FetchTrailers extends AsyncTask<Movie, Void, String[]>{

        @Override
        protected void onPreExecute() {
            showProgressBar(TRAILERS);
        }

        @Override
        protected String[] doInBackground(Movie... movieDetails) {
            if(movieDetails.length == 0)
                return null;

            Movie movieDetailsElement = movieDetails[0];
            String jsonMovieResponse;
            String [] trailers = null;

            URL url = NetworkUtils.buildUrl(movieDetailsElement.getId(), movieDetailsElement.getMovieSubInformations());

            try{
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                trailers = MoviesJsonData.getReviewsTrailersInformation(jsonMovieResponse, "key");

            }catch(IOException e){
                e.printStackTrace();
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(String[] trailers) {
            if(trailers != null){
                showSpinner(TRAILERS);
                setSpinnerAdapters(trailers.length, TRAILERS);
                trailers_keys = trailers;
            }
            else
                showErrorMessage(TRAILERS);
        }
    }

    private void loadReviewsTrailers(){
        Movie reviewsDetails = new Movie(id, REVIEWS);
        new FetchReviews().execute(reviewsDetails);

        Movie trailerDetails = new Movie(id, TRAILERS);
        new FetchTrailers().execute(trailerDetails);
    }

    public void setSpinnerAdapters(int dataLength, String key){
        switch (key){
            case(TRAILERS):
                String[] trailersLabels = new String[dataLength];

                for(int i = 0; i < dataLength; i++)
                    trailersLabels[i] = "Trailer " + (i+1);

                dAdapterTrailers = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trailersLabels);
                dAdapterTrailers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                dTrailersSpinner.setAdapter(dAdapterTrailers);
                break;
            case(REVIEWS):
                String[] reviewsLabels = new String[dataLength];

                for(int i = 0; i < dataLength; i++)
                    reviewsLabels[i] = "Review " + (i+1);

                dAdapterReviews = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reviewsLabels);
                dAdapterReviews.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                dReviewsSpinner.setAdapter(dAdapterReviews);
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if(count > 1){
            if(adapterView.equals(dTrailersSpinner)){
                String url = "https://youtu.be/" + trailers_keys[position];
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

            else if(adapterView.equals(dReviewsSpinner)){
                Context context = this;
                Class destinationClass = ReviewDetails.class;

                Intent intent = new Intent(context, destinationClass);

                intent.putExtra("reviewNumber", position+1);
                intent.putExtra("movieLabel", movieLabel);
                intent.putExtra("review", reviewsText[position]);

                startActivity(intent);
            }
        }else
            count++;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked) {
            final FavoriteMovie favoriteMovie = new FavoriteMovie(id, poster, movieLabel, overView, voteAverage, releaseDate);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    dDataBase.favoriteDao().insertFavorite(favoriteMovie);
                }
            });
        }
        else{
            //I don't need to use LiveData because I only get the database to know which movie to delete
            //So If I use LiveData this method will be execute every time the database get changed or something add which is not needed
            final List<FavoriteMovie> favoriteMoviesList = dDataBase.favoriteDao().loadAllFavoriteMoviesWithoutLiveData();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    FavoriteMovie fMObj = null;

                    for(int i = 0; i < favoriteMoviesList.size(); i++){
                        fMObj = favoriteMoviesList.get(i);

                        if(id == fMObj.getMovieId()) {
                            dDataBase.favoriteDao().deleteFavorite(fMObj);
                            break;
                        }
                    }
                }
            });
        }
    }


    private void checkIfFavorite(final Context context) {
        //I don't need to use LiveData because I only get the database to know if this selected movie is favorite or not
        //So If I use LiveData this method will be execute every time the database get changed or something add which is not needed
        final List<FavoriteMovie> favoriteMoviesList = dDataBase.favoriteDao().loadAllFavoriteMoviesWithoutLiveData();

        if(favoriteMoviesList.size() > 0) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    FavoriteMovie fMObj = null;
                    for (int i = 0; i < favoriteMoviesList.size(); i++) {
                        fMObj = favoriteMoviesList.get(i);
                        if (id == fMObj.getMovieId()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dSwitch.setOnCheckedChangeListener(null);
                                    dSwitch.setChecked(true);
                                    dSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
                                }
                            });
                            break;
                        }
                    }
                }
            });
        }
    }

    public void onClickRecommendations(View v){
        Context context = this;
        Class destinationClass = RecommendationsSimilarMovies.class;

        Intent intent = new Intent(context, destinationClass);

        intent.putExtra("movieId", id);
        intent.putExtra("movieSubInformations", "recommendations");

        startActivity(intent);
    }

    public void onClickSimilar(View v){
        Context context = this;
        Class destinationClass = RecommendationsSimilarMovies.class;

        Intent intent = new Intent(context, destinationClass);

        intent.putExtra("movieId", id);
        intent.putExtra("movieSubInformations", "similar");

        startActivity(intent);
    }

    public void updateViews(Intent intent){
        if (intent != null){
            if(intent.hasExtra(ID))
                this.id = intent.getIntExtra(ID, 100);

            if(intent.hasExtra(POSTER)) {
                poster = intent.getStringExtra(POSTER);
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w185" + poster)
                        .into(dPosterImageView);
            }

            if(intent.hasExtra(TITLE)) {
                movieLabel = intent.getStringExtra(TITLE);
                dTitleTextView.setText(movieLabel);
            }
            if(intent.hasExtra(OVERVIEW)) {
                overView = intent.getStringExtra(OVERVIEW);
                dOverviewTextView.setText(overView);

            }
            if(intent.hasExtra(VOTEAVERAGE)) {
                voteAverage = intent.getIntExtra(VOTEAVERAGE, 0);
                dUserRatingTextView.append(voteAverage+"");
            }
            if(intent.hasExtra(RELEASEDATE)){
                releaseDate = intent.getStringExtra(RELEASEDATE);
                dReleaseDateTexView.append(releaseDate);
            }
        }
    }

    private void showSpinner(String key){
        switch(key){
            case(REVIEWS):
                dReviewsSpinner.setVisibility(View.VISIBLE);
                dReviewsPB.setVisibility(View.GONE);
                dReviewsTextView.setVisibility(View.GONE);
                break;

            case(TRAILERS):
                dTrailersSpinner.setVisibility(View.VISIBLE);
                dTrailersPB.setVisibility(View.GONE);
                dTrailersTextView.setVisibility(View.GONE);
                break;
        }
    }

    private void showProgressBar(String key){
        switch(key){
            case(REVIEWS):
                dReviewsPB.setVisibility(View.VISIBLE);
                dReviewsSpinner.setVisibility(View.GONE);
                dReviewsTextView.setVisibility(View.GONE);
                break;

            case(TRAILERS):
                dTrailersPB.setVisibility(View.VISIBLE);
                dTrailersSpinner.setVisibility(View.GONE);
                dTrailersTextView.setVisibility(View.GONE);
                break;
        }
    }

    public void showErrorMessage(String key){
        switch(key){
            case(REVIEWS):
                dReviewsTextView.setVisibility(View.VISIBLE);
                dReviewsSpinner.setVisibility(View.GONE);
                dReviewsPB.setVisibility(View.GONE);
                break;

            case(TRAILERS):
                dTrailersTextView.setVisibility(View.VISIBLE);
                dTrailersSpinner.setVisibility(View.GONE);
                dTrailersPB.setVisibility(View.GONE);
                break;
        }
    }
}
