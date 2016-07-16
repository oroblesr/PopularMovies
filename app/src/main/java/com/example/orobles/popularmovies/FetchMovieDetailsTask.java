package com.example.orobles.popularmovies;

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
 * Created by oroblesr on 5/11/16.
 */
public class FetchMovieDetailsTask extends AsyncTask<Integer, Void, MovieDetails> {


    private Context mContext;
    private final int CREDITS = 0;
    private final int REVIEWS = 1;
    private final int VIDEOS = 2;


    public FetchMovieDetailsTask(Context mContext) {
        this.mContext = mContext;
    }


    private MovieCredits[] getDetailsFromCastJson(String moviesJsonStr)
            throws JSONException {

        final String TMDB_MOVIE_CAST = "cast";
        final String TMDB_MOVIE_CHARACTER = "character";
        final String TMDB_MOVIE_NAME = "name";
        final String TMDB_MOVIE_PROFILE_PATH = "profile_path";
        final String TMDB_MOVIE_CREDIT_ID = "credit_id";
        final String TMDB_MOVIE_ORDER = "order";

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr == null ? "" : moviesJsonStr);
        JSONArray movieListArray = moviesJsonObject.getJSONArray(TMDB_MOVIE_CAST);
        MovieCredits[] mMovieCredits = new MovieCredits[movieListArray.length()];

        for(int i = 0; i < movieListArray.length(); i++) {

            String character;
            String name;
            String profile_path;
            String credit_id;
            int order;

            JSONObject movie = movieListArray.getJSONObject(i);

            character = movie.getString(TMDB_MOVIE_CHARACTER);
            name = movie.getString(TMDB_MOVIE_NAME);
            profile_path = movie.getString(TMDB_MOVIE_PROFILE_PATH);
            credit_id = movie.getString(TMDB_MOVIE_CREDIT_ID);
            order = movie.getInt(TMDB_MOVIE_ORDER);

            mMovieCredits[i] = new MovieCredits();
            mMovieCredits[i].setCharacter(character);
            mMovieCredits[i].setName(name);
            mMovieCredits[i].setProfile_path(profile_path);
            mMovieCredits[i].setCredit_id(credit_id);
            mMovieCredits[i].setOrder(order);
        }

        return mMovieCredits;

}


    private MovieReviews[] getDetailsFromReviewsJson(String moviesJsonStr)
            throws JSONException {


        final String TMDB_MOVIE_REVIEWS_RESULTS = "results";
        final String TMDB_MOVIE_AUTHOR = "author";
        final String TMDB_MOVIE_CONTENT = "content";
        final String TMDB_MOVIE_REVIEW_ID= "id";

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr == null ? "" : moviesJsonStr);
        JSONArray movieListArray = moviesJsonObject.getJSONArray(TMDB_MOVIE_REVIEWS_RESULTS);


        MovieReviews[] mMovieReviews = new MovieReviews[movieListArray.length()];

        for(int i = 0; i < movieListArray.length(); i++) {

            String author;
            String content;
            String id;

            JSONObject movie = movieListArray.getJSONObject(i);

            author = movie.getString(TMDB_MOVIE_AUTHOR);
            content = movie.getString(TMDB_MOVIE_CONTENT);
            id = movie.getString(TMDB_MOVIE_REVIEW_ID);

            mMovieReviews[i] = new MovieReviews();
            mMovieReviews[i].setAuthor(author);
            mMovieReviews[i].setContent(content);
            mMovieReviews[i].setReview_id(id);
        }
        return mMovieReviews;

    }

    private MovieVideos[] getDetailsFromVideosJson(String moviesJsonStr) throws JSONException {

        final String TMDB_MOVIE_VIDEOS_RESULTS = "results";
        final String TMDB_MOVIE_KEY = "key";
        final String TMDB_MOVIE_VIDEO_NAME = "name";
        final String TMDB_MOVIE_VIDEO_ID = "id";

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr == null ? "" : moviesJsonStr);
        JSONArray movieListArray = moviesJsonObject.getJSONArray(TMDB_MOVIE_VIDEOS_RESULTS);

        MovieVideos[] mMovieVideos = new MovieVideos[movieListArray.length()];

        for (int i = 0; i < movieListArray.length(); i++) {

            String key;
            String name;
            String id;

            JSONObject movie = movieListArray.getJSONObject(i);

            key = movie.getString(TMDB_MOVIE_KEY);
            name = movie.getString(TMDB_MOVIE_VIDEO_NAME);
            id = movie.getString(TMDB_MOVIE_VIDEO_ID);

            mMovieVideos[i] = new MovieVideos();
            mMovieVideos[i].setKey(key);
            mMovieVideos[i].setName(name);
            mMovieVideos[i].setVideoId(id);
        }

        return mMovieVideos;

    }


    private String getJsonStrFromURL(String baseURL){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        InputStream inputStream = null;
        try {

            if (PMUtility.API_KEY.equals("")){
                Log.e("Popular Movies","Please add your API key");
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
                // Bad response from server, retry connection once again
                urlConnection.disconnect();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                if (responseCode >= 400 && responseCode <= 499) {
                    return null;
                }
            }
            // Read the input stream into a String
            inputStream = urlConnection.getInputStream();
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
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(PMUtility.LOG_TAG, "Error closing stream", e);
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                    Log.e(PMUtility.LOG_TAG, "Error closing stream", e);
                }
            }
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
        return moviesJsonStr;
    }


    
    @Override
    protected MovieDetails doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }

        int movieId = params[0];
        String stringMovieId = Integer.toString(params[0]);


        String movieDetailsURL[] = {
        PMUtility.TMDB_BASE_URL + stringMovieId + PMUtility.CREDITS_QUERY,
        PMUtility.TMDB_BASE_URL + stringMovieId + PMUtility.REVIEWS_QUERY,
        PMUtility.TMDB_BASE_URL + stringMovieId + PMUtility.VIDEOS_QUERY
    };

        String[] moviesJsonStr = new String[movieDetailsURL.length];

        // Get the Jason Strings for each of the details wanted
        for (int i = 0; i < movieDetailsURL.length; i++){
            moviesJsonStr[i] = getJsonStrFromURL(movieDetailsURL[i]);
        }

        try {
            MovieCredits[] mMovieCredits;
            MovieReviews[] mMovieReviews;
            MovieVideos[] mMovieVideos;
            if (moviesJsonStr[CREDITS] != null){
                mMovieCredits = getDetailsFromCastJson(moviesJsonStr[CREDITS]);
            }
            else {
                mMovieCredits =  new MovieCredits[0];
            }
            if (moviesJsonStr[REVIEWS] != null){
                mMovieReviews = getDetailsFromReviewsJson(moviesJsonStr[REVIEWS]);
            }
            else {
                mMovieReviews = new MovieReviews[0];
            }
            if (moviesJsonStr[VIDEOS] != null) {
                mMovieVideos = getDetailsFromVideosJson(moviesJsonStr[VIDEOS]);
            }
            else {
                mMovieVideos = new MovieVideos[0];
            }

            return new MovieDetails(movieId,mMovieCredits,mMovieReviews,mMovieVideos);

        } catch (JSONException e) {
            Log.e(PMUtility.LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MovieDetails MovieDetailsResult) {

        DBOperations dbOperations = new DBOperations();
        dbOperations.saveMovieDetailsToDB(mContext,MovieDetailsResult);
        dbOperations.execute();


    }
}
