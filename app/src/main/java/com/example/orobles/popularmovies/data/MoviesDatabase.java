package com.example.orobles.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by oroblesr on 5/17/16.
 */


@Database(version = MoviesDatabase.VERSION)

public final class MoviesDatabase {

    private MoviesDatabase(){}

    public static final int VERSION = 1;

    @Table(MoviesColumns.class) public static final String MOVIES = "movies";
    @Table(ReviewsColumns.class) public static final String REVIEWS = "reviews";
    @Table(CreditsColumns.class) public static final String CREDITS = "credits";
    @Table(VideosColumns.class) public static final String VIDEOS = "videos";




}
