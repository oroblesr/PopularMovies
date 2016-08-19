package com.example.orobles.popularmovies;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;

import com.example.orobles.popularmovies.data.CreditsColumns;
import com.example.orobles.popularmovies.data.MoviesColumns;
import com.example.orobles.popularmovies.data.MoviesProvider;
import com.example.orobles.popularmovies.data.ReviewsColumns;
import com.example.orobles.popularmovies.data.VideosColumns;

import java.util.ArrayList;

/**
 * Created by oroblesr on 6/10/16.
 */
public class DBOperations extends AsyncTask<Void, Void, Void> {

    // This order is given by MoviesColumns.java
    private final int MOVIE_ID = 0;
    private final int ORIGINAL_TITLE = 1;
    private final int OVERVIEW = 2;
    private final int BACKDROP_PATH = 3;
    private final int POSTER_PATH = 4;
    private final int RELEASE_DATE = 7;
    private final int VOTE_AVERAGE = 8;
    private final int VOTE_COUNT = 9;
    private final int IS_POPULAR = 10;
    private final int IS_TOP_RATED = 11;
    private final int IS_FAVORITE = 12;
    private final int POPULARITY = 13;


    private final int DETAIL_MOVIE_ID = 1;

    // This order is given by CreditsColumns.java
    private final int CREDIT_ID = 0;
    private final int CHARACTER = 2;
    private final int CAST_NAME = 3;
    private final int CREDIT_PROFILE_PATH = 4;
    private final int CREDIT_ORDER = 5;


    // This order is given by ReviewsColumns.java
    private final int REVIEW_ID = 0;
    private final int REVIEW_AUTHOR = 2;
    private final int REVIEW_CONTENT = 3;

    // This order is given by VideoColumns.java
    private final int VIDEO_ID = 0;
    private final int THUMBNAIL_URL = 2;
    private final int VIDEO_NAME = 3;
    private final int VIDEO_KEY = 4;

    private final String CREDIT_ID_COLUMN = "credit_id";
    private final String REVIEW_ID_COLUMN = "review_id";
    private final String VIDEO_ID_COLUMN = "video_id";
    private final String ID_COLUMN = "id";
    private final String CHARACTER_COLUMN = "character";
    private final String REVIEW_AUTHOR_COLUMN = "review_author";
    private final String VIDEO_NAME_COLUMN = "video_name";
    private final String DETAIL_MOVIE_ID_COLUMN = "movie_id";
    private final String IS_FAVORITE_COLUMN = "is_favorite";
    private final String IS_POPULAR_COLUMN = "is_popular";
    private final String IS_TOP_RATED_COLUMN = "is_top_rated";

    private Context mContext;
    private PopularMoviesAdapter mPMAdapter;
    private PopularMoviesParcelable[] mPMArray;
    private int movieId;

    private MovieCastAdapter mMovieCastAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;
    private MovieVideoAdapter mMovieVideoAdapter;
    private MovieDetails mMovieDetails;

    private int operation;
    final private int FAVORITE_MOVIES_IN_DB = 0;
    final private int PARCELABLE_ARRAY_TO_DB = 1;
    final private int MOVIE_AS_FAVORITE = 2;
    final private int REMOVE_MOVIE_AS_FAVORITE = 3;
    final private int POPULAR_MOVIES_IN_DB = 4;
    final private int TOP_RATED_MOVIES_IN_DB = 5;
    final private int MOVIE_DETAILS_TO_DB = 6;
    final private int MOVIE_DETAILS_IN_DB = 7;


    private PopularMoviesParcelable[] dbCursorToPopularMoviesParcelable(Cursor dbCursor) {
        PopularMoviesParcelable[] favoriteMoviesArray = new PopularMoviesParcelable[dbCursor.getCount()];

        if (dbCursor.moveToFirst()) {

            for (int i = 0; i < dbCursor.getCount(); i++) {
                favoriteMoviesArray[i] = new PopularMoviesParcelable();
                favoriteMoviesArray[i].setId(dbCursor.getInt(MOVIE_ID));
                favoriteMoviesArray[i].setOriginal_title(dbCursor.getString(ORIGINAL_TITLE));
                favoriteMoviesArray[i].setOverview(dbCursor.getString(OVERVIEW));
                favoriteMoviesArray[i].setBackdrop_path(dbCursor.getString(BACKDROP_PATH));
                favoriteMoviesArray[i].setPoster_path(dbCursor.getString(POSTER_PATH));
                favoriteMoviesArray[i].setRelease_date(dbCursor.getString(RELEASE_DATE));
                favoriteMoviesArray[i].setVote_average(dbCursor.getString(VOTE_AVERAGE));
                favoriteMoviesArray[i].setVote_count(dbCursor.getInt(VOTE_COUNT));
                favoriteMoviesArray[i].setIsPopular(dbCursor.getInt(IS_POPULAR));
                favoriteMoviesArray[i].setIsTopRated(dbCursor.getInt(IS_TOP_RATED));
                favoriteMoviesArray[i].setIsFavorite(dbCursor.getInt(IS_FAVORITE));
                favoriteMoviesArray[i].setPopularity(dbCursor.getDouble(POPULARITY));

                dbCursor.moveToNext();
            }

        } else {
            return null;
        }

        dbCursor.close();
        return favoriteMoviesArray;
    }



    public boolean movieExistsInDB(Context context, int movieId) {
        String movieSelection = ID_COLUMN + " = " + Integer.toString(movieId);

        Cursor dbCursor = context.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows


        if (dbCursor != null && dbCursor.moveToFirst()) {

            if (movieId == dbCursor.getInt(MOVIE_ID)) {
                dbCursor.close();
                return true;
            }
        }
        if (dbCursor != null) {
            dbCursor.close();
        }
        return false;
    }


    public void getFavoriteMoviesInDB(Context context, PopularMoviesAdapter popularMoviesAdapter) {
        this.mContext = context;
        this.mPMAdapter = popularMoviesAdapter;
        operation = FAVORITE_MOVIES_IN_DB;
    }

    private void favoriteMoviesInDB() {

        String movieSelection = IS_FAVORITE_COLUMN + " = " + PMUtility.INT_YES;

        Cursor dbCursor = mContext.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows

        if (dbCursor != null){
            if (dbCursor.moveToFirst()) {
                mPMArray = dbCursorToPopularMoviesParcelable(dbCursor);
            }
            dbCursor.close();
        }
    }


    public static int isFavoriteInDB(Context context, int movieId) {

        final String IS_FAVORITE_COLUMN = "is_favorite";
        final String ID_COLUMN = "id";
        final int MOVIE_ID = 0;

        String movieSelection = IS_FAVORITE_COLUMN + " = " + PMUtility.INT_YES + " AND " +
                ID_COLUMN + " = " + Integer.toString(movieId);

        Cursor dbCursor = context.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows

        if (dbCursor != null && dbCursor.moveToFirst()){
            if (movieId == dbCursor.getInt(MOVIE_ID)) {
                dbCursor.close();
               return PMUtility.INT_YES;
            }
        }
        if (dbCursor != null) {
            dbCursor.close();
        }
        return PMUtility.INT_NO;

    }


    public void getPopularMoviesInDB(Context context, PopularMoviesAdapter popularMoviesAdapter) {
        this.mContext = context;
        this.mPMAdapter = popularMoviesAdapter;
        this.operation = POPULAR_MOVIES_IN_DB;
    }

    private void popularMoviesInDB() {
        String movieSelection = IS_POPULAR_COLUMN + " = " + PMUtility.INT_YES;
        String sortOrder = "popularity" + " DESC";

        Cursor dbCursor = mContext.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                sortOrder);         // The sort order for the returned rows

        if (dbCursor != null){
            if (dbCursor.moveToFirst()) {
                mPMArray = dbCursorToPopularMoviesParcelable(dbCursor);
            }
            dbCursor.close();
        }

    }

    public void getTopRatedMoviesInDB(Context context, PopularMoviesAdapter popularMoviesAdapter) {
        this.mContext = context;
        this.mPMAdapter = popularMoviesAdapter;
        this.operation = TOP_RATED_MOVIES_IN_DB;
    }

    private void topRatedMoviesInDB() {
        String movieSelection = IS_TOP_RATED_COLUMN + " = " + PMUtility.INT_YES;
        String sortOrder = "vote_average_number" + " DESC";

        Cursor dbCursor = mContext.getContentResolver().query(
                MoviesProvider.Movies.MOVIES,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                sortOrder);         // The sort order for the returned rows

        if (dbCursor != null){
            if (dbCursor.moveToFirst()) {
                mPMArray = dbCursorToPopularMoviesParcelable(dbCursor);
            }
            dbCursor.close();
        }

    }


    public void saveParcelableArrayToDB(Context context,PopularMoviesParcelable[] PopularMoviesParcelableResult){
        this.mContext = context;
        this.mPMArray = PopularMoviesParcelableResult;
        this.operation = PARCELABLE_ARRAY_TO_DB;
    }


    private void parcelableArrayToDB() {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(mPMArray.length);
        ContentProviderOperation.Builder builder;

        for (PopularMoviesParcelable movie : mPMArray) {
            builder = ContentProviderOperation.newInsert(MoviesProvider.Movies.MOVIES);

            if (!movieExistsInDB(mContext, movie.getId())) {
                builder.withValue(MoviesColumns._ID, movie.getId());
                builder.withValue(MoviesColumns.ORIGINAL_TITLE, movie.getOriginal_title());
                builder.withValue(MoviesColumns.OVERVIEW, movie.getOverview());
                builder.withValue(MoviesColumns.BACKDROP_PATH, movie.getBackdrop_path());
                builder.withValue(MoviesColumns.POSTER_PATH, movie.getPoster_path());
                builder.withValue(MoviesColumns.BACKDROP_PATH_URL, movie.getBackdrop_path_url());
                builder.withValue(MoviesColumns.POSTER_PATH_URL, movie.getPoster_path_url());
                builder.withValue(MoviesColumns.RELEASE_DATE, movie.getRelease_date());
                builder.withValue(MoviesColumns.VOTE_AVERAGE, movie.getVote_average());
                builder.withValue(MoviesColumns.VOTE_COUNT, movie.getVote_count());
                builder.withValue(MoviesColumns.IS_POPULAR, movie.getIsPopular());
                builder.withValue(MoviesColumns.IS_TOP_RATED, movie.getIsTopRated());
                builder.withValue(MoviesColumns.IS_FAVORITE, movie.getIsFavorite());
                builder.withValue(MoviesColumns.VOTE_AVERAGE_NUMBER, movie.getVote_average_number());
                builder.withValue(MoviesColumns.POPULARITY, movie.getPopularity());
                batchOperations.add(builder.build());
            }
        }

        try {
            mContext.getContentResolver().applyBatch(MoviesProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

    }

    public boolean creditExistsInDB(Context context, String credit_id) {
        String movieSelection = CREDIT_ID_COLUMN + " = ?" ;
        String[] selectArgs = {credit_id};

        Cursor dbCursor = context.getContentResolver().query(
                MoviesProvider.Credits.CREDITS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                selectArgs,         // Selection criteria
                null);              // The sort order for the returned rows


        if (dbCursor != null && dbCursor.moveToFirst()) {

            if (credit_id.equals(dbCursor.getString(CREDIT_ID))) {
                dbCursor.close();
                return true;
            }
        }
        if (dbCursor != null) {
            dbCursor.close();
        }
        return false;
    }

    public boolean reviewExistsInDB(Context context, String review_id) {
        String movieSelection = REVIEW_ID_COLUMN + " = ?" ;
        String[] selectArgs = {review_id};

        Cursor dbCursor = context.getContentResolver().query(
                MoviesProvider.Reviews.REVIEWS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                selectArgs,         // Selection criteria
                null);              // The sort order for the returned rows


        if (dbCursor != null && dbCursor.moveToFirst()) {

            if (review_id.equals(dbCursor.getString(REVIEW_ID))) {
                dbCursor.close();
                return true;
            }
        }
        if (dbCursor != null) {
            dbCursor.close();
        }
        return false;
    }

    public boolean videoExistsInDB(Context context, String video_id) {
        String movieSelection = VIDEO_ID_COLUMN + " = ? ";
        String[] selectArgs = {video_id};

        Cursor dbCursor = context.getContentResolver().query(
                MoviesProvider.Videos.VIDEOS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                selectArgs,         // Selection criteria
                null);              // The sort order for the returned rows


        if (dbCursor != null && dbCursor.moveToFirst()) {

            if (video_id.equals(dbCursor.getString(VIDEO_ID))) {
                dbCursor.close();
                return true;
            }
        }
        if (dbCursor != null) {
            dbCursor.close();
        }
        return false;
    }



    public void saveMovieDetailsToDB(Context context,MovieDetails movieDetails){
        this.mContext = context;
        this.mMovieDetails = movieDetails;
        this.operation = MOVIE_DETAILS_TO_DB;
    }

    private void movieDetailsToDB() {
        int batchLength = mMovieDetails.getMovieCredits().length
                + mMovieDetails.getMovieReviews().length
                + mMovieDetails.getMovieVideos().length;

        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(batchLength);
        ContentProviderOperation.Builder builder;

        for (MovieCredits credit : mMovieDetails.getMovieCredits()) {
            if (!creditExistsInDB(mContext, credit.getCredit_id())){
                builder = ContentProviderOperation.newInsert(MoviesProvider.Credits.CREDITS);

                builder.withValue(CreditsColumns._ID, credit.getCredit_id());
                builder.withValue(CreditsColumns.MOVIE_ID, mMovieDetails.getMovieId());
                builder.withValue(CreditsColumns.CHARACTER, credit.getCharacter());
                builder.withValue(CreditsColumns.CAST_NAME, credit.getName());
                builder.withValue(CreditsColumns.PROFILE_PATH, credit.getProfile_path());
                builder.withValue(CreditsColumns.PROFILE_PATH_URL, credit.getProfile_path_url());
                builder.withValue(CreditsColumns.CREDIT_ORDER, credit.getOrder());

                batchOperations.add(builder.build());
            }
        }

        for (MovieReviews review : mMovieDetails.getMovieReviews()) {
            if (!reviewExistsInDB(mContext,review.getReview_id())){
                builder = ContentProviderOperation.newInsert(MoviesProvider.Reviews.REVIEWS);

                builder.withValue(ReviewsColumns._ID, review.getReview_id());
                builder.withValue(ReviewsColumns.MOVIE_ID, mMovieDetails.getMovieId());
                builder.withValue(ReviewsColumns.REVIEW_AUTHOR, review.getAuthor());
                builder.withValue(ReviewsColumns.REVIEW_CONTENT, review.getContent());

                batchOperations.add(builder.build());
            }
        }

        for (MovieVideos video : mMovieDetails.getMovieVideos()) {
            if (!videoExistsInDB(mContext,video.getVideoId())){
                builder = ContentProviderOperation.newInsert(MoviesProvider.Videos.VIDEOS);

                builder.withValue(VideosColumns._ID, video.getVideoId());
                builder.withValue(VideosColumns.MOVIE_ID, mMovieDetails.getMovieId());
                builder.withValue(VideosColumns.THUMBNAIL_URL, video.getThumbnailURL());
                builder.withValue(VideosColumns.VIDEO_NAME, video.getName());
                builder.withValue(VideosColumns.VIDEO_KEY, video.getKey());

                batchOperations.add(builder.build());
            }
        }

        try {
            mContext.getContentResolver().applyBatch(MoviesProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }


    }



    private MovieCredits[] dbCursorToMovieCredits(Cursor dbCursor) {
        MovieCredits[] creditsArray = new MovieCredits[dbCursor.getCount()];

        if (dbCursor.moveToFirst()) {
            for (int i = 0; i < dbCursor.getCount(); i++) {
                creditsArray[i] = new MovieCredits();
                creditsArray[i].setCharacter(dbCursor.getString(CHARACTER));
                creditsArray[i].setName(dbCursor.getString(CAST_NAME));
                creditsArray[i].setProfile_path(dbCursor.getString(CREDIT_PROFILE_PATH));
                creditsArray[i].setCredit_id(dbCursor.getString(CREDIT_ID));
                creditsArray[i].setOrder(dbCursor.getInt(CREDIT_ORDER));
                dbCursor.moveToNext();
            }
        } else {
            return null;
        }
        dbCursor.close();
        return creditsArray;
    }


    private MovieReviews[] dbCursorToMovieReviews(Cursor dbCursor) {
        MovieReviews[] reviewsArray = new MovieReviews[dbCursor.getCount()];

        if (dbCursor.moveToFirst()) {
            for (int i = 0; i < dbCursor.getCount(); i++) {
                reviewsArray[i] = new MovieReviews();
                reviewsArray[i].setAuthor(dbCursor.getString(REVIEW_AUTHOR));
                reviewsArray[i].setContent(dbCursor.getString(REVIEW_CONTENT));
                dbCursor.moveToNext();
            }
        } else {
            return null;
        }
        dbCursor.close();
        return reviewsArray;
    }

    private MovieVideos[] dbCursorToMovieVideos(Cursor dbCursor) {
        MovieVideos[] videosArray = new MovieVideos[dbCursor.getCount()];

        if (dbCursor.moveToFirst()) {
            for (int i = 0; i < dbCursor.getCount(); i++) {
                videosArray[i] = new MovieVideos();
                videosArray[i].setName(dbCursor.getString(VIDEO_NAME));
                videosArray[i].setKey(dbCursor.getString(VIDEO_KEY));
                dbCursor.moveToNext();
            }
        } else {
            return null;
        }
        dbCursor.close();
        return videosArray;
    }

    public void getMovieDetailsInDB (Context context,
                                     int id,
                                     MovieCastAdapter movieCastAdapter,
                                     MovieReviewAdapter movieReviewAdapter,
                                     MovieVideoAdapter movieVideoAdapter) {
        this.mContext = context;
        this.movieId = id;
        this.mMovieCastAdapter = movieCastAdapter;
        this.mMovieReviewAdapter = movieReviewAdapter;
        this.mMovieVideoAdapter = movieVideoAdapter;
        this.operation = MOVIE_DETAILS_IN_DB;
    }

    private void movieDetailsInDB (){

        String movieSelection = DETAIL_MOVIE_ID_COLUMN + " = " + movieId;

        MovieCredits[] movieCredits;
        MovieReviews[] movieReviews;
        MovieVideos[] movieVideos ;

        Cursor creditsCursor = mContext.getContentResolver().query(
                MoviesProvider.Credits.CREDITS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows


        if (creditsCursor != null && creditsCursor.moveToFirst()) {
            movieCredits = dbCursorToMovieCredits(creditsCursor);
        }
        else {
            movieCredits = new MovieCredits[0];
        }
        Cursor reviewsCursor = mContext.getContentResolver().query(
                MoviesProvider.Reviews.REVIEWS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows

        if (reviewsCursor != null && reviewsCursor.moveToFirst()) {
            movieReviews = dbCursorToMovieReviews(reviewsCursor);
        }
        else {
            movieReviews = new MovieReviews[0];
        }

        Cursor videosCursor = mContext.getContentResolver().query(
                MoviesProvider.Videos.VIDEOS,
                null,               // The columns to return for each row
                movieSelection,     // Selection criteria
                null,               // Selection criteria
                null);              // The sort order for the returned rows

        if (videosCursor != null && videosCursor.moveToFirst()) {
            movieVideos = dbCursorToMovieVideos(videosCursor);
        }
        else {
            movieVideos = new MovieVideos[0];
        }
        mMovieDetails = new MovieDetails(movieId, movieCredits, movieReviews, movieVideos);

        if (creditsCursor != null) {
            creditsCursor.close();
        }
        if (reviewsCursor != null) {
            reviewsCursor.close();
        }
        if (videosCursor != null) {
            videosCursor.close();
        }


    }



    public void addMovieAsFavoriteInDB(Context context,int movieId){
        this.mContext = context;
        this.movieId = movieId;
        this.operation = MOVIE_AS_FAVORITE;

    }

    private void movieAsFavoriteInDB( ) {
        if (movieExistsInDB(mContext, movieId)) {

            String movieSelection = PMUtility._ID + " = " + Integer.toString(movieId);
            ContentValues contentValues = new ContentValues();

            contentValues.put(MoviesColumns.IS_FAVORITE, PMUtility.INT_YES);

            mContext.getContentResolver().update(
                    MoviesProvider.Movies.MOVIES,
                    contentValues,          // the columns to update
                    movieSelection,          // the column to select on
                    null                     // the value to compare to

            );

            mContext.getContentResolver().notifyChange(MoviesProvider.Movies.MOVIES,null);
        }
    }

    public void removeMovieAsFavoriteInDB(Context context,int movieId){
        this.mContext = context;
        this.movieId = movieId;
        this.operation = REMOVE_MOVIE_AS_FAVORITE;
    }


    private void removeMovieAsFavoriteInDB() {
        if (movieExistsInDB(mContext,movieId)) {

            String movieSelection = PMUtility._ID + " = " + Integer.toString(movieId);
            ContentValues contentValues = new ContentValues();

            contentValues.put(MoviesColumns.IS_FAVORITE, PMUtility.INT_NO);

            mContext.getContentResolver().update(
                    MoviesProvider.Movies.MOVIES,
                    contentValues,          // the columns to update
                    movieSelection,          // the column to select on
                    null                     // the value to compare to

            );

            mContext.getContentResolver().notifyChange(MoviesProvider.Movies.MOVIES,null);
        }
    }




    @Override
    protected Void doInBackground(Void... params) {

        // Update the operation in each method
        switch (operation) {
            case FAVORITE_MOVIES_IN_DB:
                favoriteMoviesInDB();
                break;
            case PARCELABLE_ARRAY_TO_DB:
                parcelableArrayToDB();
                break;
            case MOVIE_AS_FAVORITE:
                movieAsFavoriteInDB();
                break;
            case REMOVE_MOVIE_AS_FAVORITE:
                removeMovieAsFavoriteInDB();
                break;
            case POPULAR_MOVIES_IN_DB:
                popularMoviesInDB();
                break;
            case TOP_RATED_MOVIES_IN_DB:
                topRatedMoviesInDB();
                break;
            case MOVIE_DETAILS_TO_DB:
                movieDetailsToDB();
                break;
            case MOVIE_DETAILS_IN_DB:
                movieDetailsInDB();
                break;
            default:
                return null;
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Update the adapter if exists
        if (mPMAdapter != null){
            mPMAdapter.setMPopularMoviesParcelableArray(mPMArray);
            mPMAdapter.notifyDataSetChanged();
        }
        if (operation == MOVIE_DETAILS_IN_DB && mMovieDetails != null){
            mMovieCastAdapter.setMovieDetailsArray(mMovieDetails);
            mMovieCastAdapter.notifyDataSetChanged();

            mMovieReviewAdapter.setMovieDetailsArray(mMovieDetails);
            mMovieReviewAdapter.notifyDataSetChanged();

            mMovieVideoAdapter.setMovieDetailsArray(mMovieDetails);
            mMovieVideoAdapter.notifyDataSetChanged();
        }




    }
}


