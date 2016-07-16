package com.example.orobles.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by oroblesr on 5/19/16.
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY,database = MoviesDatabase.class)

public class MoviesProvider {

    public static final String AUTHORITY = "com.example.orobles.popularmovies.data.MoviesProvider";

    @TableEndpoint(table = MoviesDatabase.MOVIES) public static class Movies {
        @ContentUri(
                path = "movies",
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MoviesColumns.ORIGINAL_TITLE + " ASC")
        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/movies");
    }

    @TableEndpoint(table = MoviesDatabase.CREDITS) public static class Credits {
        @ContentUri(
                path = "credits",
                type = "vnd.android.cursor.dir/credits",
                defaultSort = CreditsColumns.CREDIT_ORDER + " ASC")
        public static final Uri CREDITS = Uri.parse("content://" + AUTHORITY + "/credits");
    }

    @TableEndpoint(table = MoviesDatabase.REVIEWS) public static class Reviews {
        @ContentUri(
                path = "reviews",
                type = "vnd.android.cursor.dir/reviews",
                defaultSort = ReviewsColumns.MOVIE_ID + " ASC")
        public static final Uri REVIEWS = Uri.parse("content://" + AUTHORITY + "/reviews");
    }

    @TableEndpoint(table = MoviesDatabase.VIDEOS) public static class Videos {
        @ContentUri(
                path = "videos",
                type = "vnd.android.cursor.dir/videos",
                defaultSort = VideosColumns.VIDEO_NAME + " ASC")
        public static final Uri VIDEOS = Uri.parse("content://" + AUTHORITY + "/videos");
    }
}
