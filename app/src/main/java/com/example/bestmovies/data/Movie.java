package com.example.bestmovies.data;

public class Movie {

    private int id;
    private String poster;
    private String title;
    private String overview;
    private int voteAverage;
    private String releaseDate;
    private String movieSubInformations;

    public Movie(int id, String poster, String title, String overview, int voteAverage, String releaseDate){
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public Movie(int id, String movieSubInformations){
        this.id = id;
        this.movieSubInformations = movieSubInformations;
    }

    public int getId() {
        return id;
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

    public String getMovieSubInformations(){
        return movieSubInformations;
    }
}
