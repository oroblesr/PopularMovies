package com.example.orobles.popularmovies;

/**
 * Created by oroblesr on 5/18/16.
 */
public class MovieReviews {

    /*
    When making changes, please update at least the following:
    - ReviewsColumns.java
    - FetchMovieDetailsTask.java
    - DBOperations.java
    */

    private String author;
    private String content;
    private String review_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }
}
