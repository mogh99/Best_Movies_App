package com.example.bestmovies.utilities;

import android.net.Uri;

import com.example.bestmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String API_PERSONAL_KEY = "";

    final static String APIKEY = "api_key";
    final static String PAGE = "page";

    //build the url for the category
    //think about how to retrieve more than one page
    public static URL buildUrl(String category, int page){
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(category)
                .appendQueryParameter(APIKEY, API_PERSONAL_KEY)
                .appendQueryParameter(PAGE, page+"")
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }


        return url;
    }

    //build the url for Recommendations, Similar, Trailers, or Reviews
    public static URL buildUrl(int movieId, String movieSubInformations){
        Uri builtUri = null;

        builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(movieId+"")
                .appendPath(movieSubInformations)
                .appendQueryParameter(APIKEY, API_PERSONAL_KEY)
                .build();

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;

        }
        finally {
            urlConnection.disconnect();
        }
    }
}

