package com.example.orobles.popularmovies;

/**
 * Created by oroblesr on 5/18/16.
 */
public class MovieVideos {
    /*
    When making changes, please update at least the following:
    - VideosColumns.java
    - FetchMovieDetailsTask.java
    - DBOperations.java
    */

    private String name;
    private String key;
    private String thumbnailURL;
    private String videoId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.thumbnailURL = PMUtility.YOUTUBE_THUMBNAIL_URL + this.key +
                PMUtility.YOUTUBE_THUMBNAIL_QUALITY;
    }


    public String getThumbnailURL() {
        return thumbnailURL;
    }


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
