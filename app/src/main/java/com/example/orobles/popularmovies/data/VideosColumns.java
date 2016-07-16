package com.example.orobles.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by oroblesr on 6/16/16.
 */
public interface VideosColumns {
    @DataType(DataType.Type.TEXT) @PrimaryKey String _ID = "video_id";
    @DataType(DataType.Type.INTEGER) @References(table = MoviesDatabase.MOVIES,
            column = MoviesColumns._ID) @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) String THUMBNAIL_URL = "thumbnail_url";
    @DataType(DataType.Type.TEXT) String VIDEO_NAME = "video_name";
    @DataType(DataType.Type.TEXT) String VIDEO_KEY = "video_key";
}
