package com.example.twanist.project_movie;


public class MovieData {
    private String poster_path;
    private String backdrop_path;
    private String title;

    private String release_date;
    private String overview;
    private int Id;
    private double vote_ava;

    public MovieData() {
    }

    public MovieData(String poster_path, String backdrop_path, String title, String release_date, double vote_ava, String overview, int Id) {
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.release_date = release_date;
        this.vote_ava = vote_ava;
        this.overview = overview;
        this.Id = Id;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_ava() {
        return vote_ava;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getId() {
        return Id;
    }
}
