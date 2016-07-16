package com.example.orobles.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by oroblesr on 5/5/16.
 */

public class FetchPopularMoviesTask extends AsyncTask<Integer, Void, PopularMoviesParcelable[]> {


    private final int POPULAR_SECTION = 0;
    private final int TOP_RATED_SECTION = 1;
    private final int FAVORITES_SECTION = 2;

    private int section;

    private PopularMoviesAdapter mPMAdapter;
    private Context mContext;
    private Activity mActivity;

    public FetchPopularMoviesTask ( Activity activity, PopularMoviesAdapter popularMoviesAdapter){
        mPMAdapter = popularMoviesAdapter;
        mActivity = activity;
        mContext = activity.getApplicationContext();
    }

    private PopularMoviesParcelable[] getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String TMDB_MOVIE_LIST = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_ORIGINAL_TITLE= "original_title";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_VOTE_COUNT = "vote_count";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_ID = "id";

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
        JSONArray movieListArray = moviesJsonObject.getJSONArray(TMDB_MOVIE_LIST);

        PopularMoviesParcelable[] pMArray =
                new PopularMoviesParcelable[movieListArray.length()];


        for(int i = 0; i < movieListArray.length(); i++) {
            String backdrop_path;
            String original_title;
            String overview;
            String poster_path;
            String release_date;
            String vote_average;
            int vote_count;
            int id;
            double popularity;

            JSONObject movie = movieListArray.getJSONObject(i);

            backdrop_path = movie.getString(TMDB_BACKDROP_PATH);
            original_title = movie.getString(TMDB_ORIGINAL_TITLE);
            overview = movie.getString(TMDB_OVERVIEW);
            poster_path = movie.getString(TMDB_POSTER_PATH);
            release_date = movie.getString(TMDB_RELEASE_DATE);
            vote_average = movie.getString(TMDB_VOTE_AVERAGE);
            vote_count = movie.getInt(TMDB_VOTE_COUNT);
            popularity = movie.getDouble(TMDB_POPULARITY);
            id = movie.getInt(TMDB_ID);

            pMArray[i] = new PopularMoviesParcelable();
            pMArray[i].setId(id);
            pMArray[i].setBackdrop_path(backdrop_path);
            pMArray[i].setOriginal_title(original_title);
            pMArray[i].setOverview(overview);
            pMArray[i].setPoster_path(poster_path);
            pMArray[i].setRelease_date(release_date);
            pMArray[i].setVote_average(vote_average);
            pMArray[i].setVote_count(vote_count);
            pMArray[i].setPopularity(popularity);

            pMArray[i].setIsPopular(section == POPULAR_SECTION ? PMUtility.INT_YES
                    : PMUtility.INT_NO);
            pMArray[i].setIsTopRated(section == TOP_RATED_SECTION ? PMUtility.INT_YES
                    : PMUtility.INT_NO);

            pMArray[i].setIsFavorite(PMUtility.INT_NO);
        }

        return pMArray;

    }


    @Override
    protected PopularMoviesParcelable[] doInBackground(Integer... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        section = params[0];

        try {

            String baseURL;
            switch (section){
                case POPULAR_SECTION:
                    baseURL = PMUtility.POPULAR_MOVIES_URL;
                    break;
                case TOP_RATED_SECTION:
                    baseURL = PMUtility.TOP_RATED_MOVIES_URL;
                    break;

                default:
                    return null;
            }

            Uri builtUri = Uri.parse(baseURL).buildUpon()
                    .appendQueryParameter(PMUtility.API_KEY_PARAM, PMUtility.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode >= 400 && responseCode <= 499){
                // Bad response from server, retry connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                if (responseCode >= 400 && responseCode <= 499) {
                    return null;
                }
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream is empty
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(PMUtility.LOG_TAG, "Error closing stream", e);
            return null;
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(PMUtility.LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(PMUtility.LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(PopularMoviesParcelable[] pMArray) {
        if (pMArray != null){

            DBOperations dbOperations = new DBOperations();
            mPMAdapter.setMPopularMoviesParcelableArray(pMArray);
            mPMAdapter.notifyDataSetChanged();

            dbOperations.saveParcelableArrayToDB(mContext,pMArray);
            dbOperations.execute();


            for (PopularMoviesParcelable movie:pMArray){
                FetchMovieDetailsTask fetchMovieDetailsTask = new FetchMovieDetailsTask(mContext);
                fetchMovieDetailsTask.execute(movie.getId());
            }
            PMUtility.saveUpdatedSectionDay(mActivity, section);

        }
    }
}