package com.example.bestmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewDetails extends AppCompatActivity {

    private static final String REVIEW_NUMBER = "reviewNumber";
    private static final String MOVIE_LABEL = "movieLabel";
    private static final String REVIEW = "review";

    private TextView rReviewNumber;
    private TextView rMovieLabel;
    private TextView rMovieReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        rReviewNumber = (TextView) findViewById(R.id.tv_review_number);
        rMovieLabel = (TextView) findViewById(R.id.tv_movie_label);
        rMovieReview = (TextView) findViewById(R.id.tv_movie_review);

        updateViews(getIntent());
    }

    public void updateViews(Intent intent){
        if (intent != null){
            if(intent.hasExtra(REVIEW_NUMBER)) {
                int reviewNumber = intent.getIntExtra(REVIEW_NUMBER, 0);
                rReviewNumber.setText("Review " + reviewNumber);
            }

            if(intent.hasExtra(MOVIE_LABEL)){
                String movieLabel = intent.getStringExtra(MOVIE_LABEL);
                rMovieLabel.setText(movieLabel);
            }

            if(intent.hasExtra(REVIEW)){
                String reviewText = intent.getStringExtra(REVIEW);
                rMovieReview.setText(reviewText);
            }
        }
    }
}
