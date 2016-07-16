package com.example.orobles.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by oroblesr on 6/16/16.
 */
public interface CreditsColumns {
    @DataType(DataType.Type.TEXT) @PrimaryKey String _ID = "credit_id";
    @DataType(DataType.Type.INTEGER) @References(table = MoviesDatabase.MOVIES,
            column = MoviesColumns._ID) @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) String CHARACTER = "character";
    @DataType(DataType.Type.TEXT) String CAST_NAME = "cast_name";
    @DataType(DataType.Type.TEXT) String PROFILE_PATH = "profile_path";
    @DataType(DataType.Type.TEXT) String PROFILE_PATH_URL = "profile_path_url";
    @DataType(DataType.Type.INTEGER) String CREDIT_ORDER = "credit_order";

}
