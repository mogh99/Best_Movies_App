package com.example.bestmovies.utilities;

import com.example.bestmovies.data.Movie;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoviesJsonData {

    static final String JSONARRAY_RESULTS = "results";
    static final String POSTER_PATH = "poster_path";
    static final String TITLE = "title";
    static final String OVERVIEW = "overview";
    static final String USER_RATING = "vote_average";
    static final String RELEASE_DATE = "release_date";
    static final String ID = "id";



    public static Movie [] getInformationFromJsonString(String jsonText){

        //When there is no connection or something went wrong
        if(jsonText == null)
            return null;

        Movie [] moviesData = null;
        int id;
        String poster;
        String title;
        String overView;
        int voteAverage;
        String releaseDate;


        try {
            JSONObject root = new JSONObject(jsonText);

            JSONArray results = root.optJSONArray(JSONARRAY_RESULTS);
            JSONObject resultsObject = null;
            moviesData = new Movie[results.length()];

            for(int i = 0; i < results.length(); i++){
                resultsObject = results.optJSONObject(i);

                id = resultsObject.optInt(ID);
                poster = resultsObject.optString(POSTER_PATH);
                title = resultsObject.optString(TITLE);
                overView = resultsObject.optString(OVERVIEW);
                voteAverage = resultsObject.optInt(USER_RATING);
                releaseDate = resultsObject.optString(RELEASE_DATE);
                moviesData[i] = new Movie (id, poster, title, overView, voteAverage, releaseDate);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        return moviesData;
    }

    public static String[] getReviewsTrailersInformation(String jsonText, String key){

        if(jsonText == null)
            return null;

        String[] information = null;

        try{
            JSONObject root = new JSONObject(jsonText);

            JSONArray results = root.optJSONArray(JSONARRAY_RESULTS);

            if(results.length() == 0)
                return null;

            JSONObject resultsObject = null;
            information = new String[results.length()];

            for(int i = 0; i < results.length(); i++){
                resultsObject = results.optJSONObject(i);

                information[i] = resultsObject.optString(key);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return information;

    }

}
