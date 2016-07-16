package com.example.orobles.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by oroblesr on 5/20/16.
 */
public interface MoviesColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey String _ID = "id";

    @DataType(DataType.Type.TEXT) @NotNull String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT) @NotNull String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT)  String BACKDROP_PATH = "backdrop_path";
    @DataType(DataType.Type.TEXT)  String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.TEXT)  String BACKDROP_PATH_URL = "backdrop_path_url";
    @DataType(DataType.Type.TEXT) String POSTER_PATH_URL = "poster_path_url";
    @DataType(DataType.Type.TEXT) String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT)  String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.INTEGER) String VOTE_COUNT = "vote_count";
    @DataType(DataType.Type.INTEGER) String IS_POPULAR = "is_popular";
    @DataType(DataType.Type.INTEGER) String IS_TOP_RATED = "is_top_rated";
    @DataType(DataType.Type.INTEGER) String IS_FAVORITE = "is_favorite";
    @DataType(DataType.Type.REAL) String POPULARITY = "popularity";
    @DataType(DataType.Type.REAL) String VOTE_AVERAGE_NUMBER = "vote_average_number";



}
