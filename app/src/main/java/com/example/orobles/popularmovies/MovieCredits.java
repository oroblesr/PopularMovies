package com.example.orobles.popularmovies;

/**
 * Created by oroblesr on 5/11/16.
 */
public class MovieCredits {
    /*
    When making changes, please update at least the following:
    - CreditsColumns.java
    - FetchMovieDetailsTask.java
    - DBOperations.java
     */

    private String character;
    private String name;
    private String profile_path;
    private String profile_path_url;
    private String credit_id;
    private int order;


    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
        this.profile_path_url = PMUtility.PROFILE_IMAGE_SIZE_URL + profile_path;
    }

    public String getProfile_path_url() {
        return profile_path_url;
    }
}
