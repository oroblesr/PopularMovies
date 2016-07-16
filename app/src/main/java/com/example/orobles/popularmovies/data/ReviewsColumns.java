package com.example.orobles.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by oroblesr on 5/31/16.
 */
public interface ReviewsColumns {


    @DataType(DataType.Type.TEXT) @PrimaryKey  String _ID = "review_id";
    @DataType(DataType.Type.INTEGER) @References(table = MoviesDatabase.MOVIES,
            column = MoviesColumns._ID) @NotNull String MOVIE_ID = "movie_id";


    @DataType(DataType.Type.TEXT) String REVIEW_AUTHOR = "review_author";
    @DataType(DataType.Type.TEXT) String REVIEW_CONTENT = "review_content";


}
