package com.example.bestmovies.dataBase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "favorite")
public class FavoriteMovie {



    @PrimaryKey (autoGenerate = true)
    private int dataBaseId;
    private int movieId;
    private String poster;
    private String title;
    private String overview;
    private int voteAverage;
    private String releaseDate;

    @Ignore
    public FavoriteMovie(int movieId, String poster, String title, String overview, int voteAverage, String releaseDate){
        this.movieId = movieId;
        this.poster = poster;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }


    public FavoriteMovie(int dataBaseId, int movieId, String poster, String title, String overview, int voteAverage, String releaseDate){
        this.dataBaseId = dataBaseId;
        this.movieId = movieId;
        this.poster = poster;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public void setDataBaseId(int dataBaseId){
        this.dataBaseId = dataBaseId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDataBaseId() {
        return dataBaseId;
    }

    public int getMovieId() {
        return movieId;
    }
    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
