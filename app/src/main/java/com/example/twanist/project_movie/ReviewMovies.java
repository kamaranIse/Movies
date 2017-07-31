package com.example.twanist.project_movie;


public class ReviewMovies {
    private String author;
    private String content;

    public ReviewMovies(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }
}
