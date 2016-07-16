package com.example.orobles.popularmovies;

/**
 * Created by oroblesr on 5/18/16.
 */
public class MovieDetails  {

    private MovieCredits[] movieCredits;
    private MovieReviews[] movieReviews;
    private MovieVideos[] movieVideos;
    private int movieId;


    public MovieDetails(int movieId, MovieCredits[] movieCredits,
                        MovieReviews[] movieReviews,
                        MovieVideos[] movieVideos) {
        this.movieId = movieId;
        this.movieCredits = movieCredits;
        this.movieReviews = movieReviews;
        this.movieVideos = movieVideos;
    }

    public int getMovieId() {
        return movieId;
    }

    public MovieCredits[] getMovieCredits() {
        return movieCredits;
    }

    public MovieReviews[] getMovieReviews() {
        return movieReviews;
    }

    public MovieVideos[] getMovieVideos() {
        return movieVideos;
    }

}
