package com.example.orobles.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oroblesr on 5/4/16.
 */
public class PopularMoviesParcelable implements Parcelable {
    /*
    To add a new Value, please modify the following:
        * This Class constructor
        * The method writeToParcel
        * If Necessary:
        * Update the info retrieved on FetchPopularMoviesTask
        * Add a new column to MoviesColumns
        * Update DBOperations to store/retrieve new data
        * (method parcelableArrayToDB and
        * dbCursorToPopularMoviesParcelable)
        *
    */
    private String backdrop_filename;
    private String backdrop_path;
    private String backdrop_path_url;
    private String original_title;
    private String overview;
    private String poster_filename;
    private String poster_path;
    private String poster_path_url;
    private String release_date;
    private String vote_average;
    private int vote_count;
    private int id;

    private int isPopular;
    private int isTopRated;
    private int isFavorite;

    private double vote_average_number;

    private double popularity;

    public static final Creator<PopularMoviesParcelable> CREATOR =
            new Creator<PopularMoviesParcelable>() {
                @Override
                public PopularMoviesParcelable createFromParcel(Parcel in) {
                    return new PopularMoviesParcelable(in);
                }

                @Override
                public PopularMoviesParcelable[] newArray(int size) {
                    return new PopularMoviesParcelable[size];
                }
            };

    protected PopularMoviesParcelable(Parcel in) {
        backdrop_filename = in.readString();
        backdrop_path = in.readString();
        backdrop_path_url = in.readString();
        original_title = in.readString();
        overview = in.readString();
        poster_filename = in.readString();
        poster_path = in.readString();
        poster_path_url = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        vote_count = in.readInt();
        id = in.readInt();
        isPopular = in.readInt();
        isTopRated = in.readInt();
        isFavorite = in.readInt();
        vote_average_number = in.readDouble();
        popularity = in.readDouble();

    }

    // Keep in the same order as the constructor above
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdrop_filename);
        dest.writeString(backdrop_path);
        dest.writeString(backdrop_path_url);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(poster_filename);
        dest.writeString(poster_path);
        dest.writeString(poster_path_url);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeInt(isPopular);
        dest.writeInt(isTopRated);
        dest.writeInt(isFavorite);
        dest.writeDouble(vote_average_number);
        dest.writeDouble(popularity);
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
        this.backdrop_path_url = PMUtility.BACKDROP_IMAGE_SIZE_URL + backdrop_path;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
        this.vote_average_number = Double.valueOf(vote_average);
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
        this.poster_path_url = PMUtility.POSTER_IMAGE_SIZE_URL + poster_path;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setId(int id) {
        this.id = id;
        this.backdrop_filename = String.valueOf(id) + PMUtility.BACKDROP + PMUtility.FORMAT;
        this.poster_filename = String.valueOf(id) + PMUtility.POSTER + PMUtility.FORMAT;

    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getBackdrop_path_url() {
        return backdrop_path_url;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getPoster_path_url() {
        return poster_path_url;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getBackdrop_filename() {
        return backdrop_filename;
    }

    public String getPoster_filename() {
        return poster_filename;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public PopularMoviesParcelable(){
    }

    public int getId() {
        return id;
    }

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getIsTopRated() {
        return isTopRated;
    }

    public void setIsTopRated(int isTopRated) {
        this.isTopRated = isTopRated;
    }

    public double getVote_average_number() {
        return vote_average_number;
    }


}
