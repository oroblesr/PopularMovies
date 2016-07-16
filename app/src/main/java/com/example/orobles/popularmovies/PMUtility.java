package com.example.orobles.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oroblesr on 5/12/16.
 */
public class PMUtility {

    //TODO Add your APIKEY
    public static final String API_KEY = "";

    public static final String _ID = "id";


    public static final int INT_YES = 1;
    public static final int INT_NO = 0;

    public static final String FORMAT = ".png";
    public static final String POSTER = "_poster";
    public static final String BACKDROP = "_backdrop";

    // Available sizes: "w92/", "w154/", "w185/", "w342/", "w500/", "w780/", or "original/"

    public static final String PROFILE_IMAGE_SIZE = "w92/";
    public static final String BACKDROP_IMAGE_SIZE = "w342/";
    public static final String POSTER_IMAGE_SIZE = "w500/";


    public static final String PROFILE_IMAGE_SIZE_URL = "http://image.tmdb.org/t/p/" +
                                                             PROFILE_IMAGE_SIZE;

    public static final String  BACKDROP_IMAGE_SIZE_URL = "http://image.tmdb.org/t/p/" +
                                                             BACKDROP_IMAGE_SIZE;

    public static final String  POSTER_IMAGE_SIZE_URL = "http://image.tmdb.org/t/p/" +
                                                            POSTER_IMAGE_SIZE;


    public static final String TMDB_BASE_URL= "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular?";
    public static final String TOP_RATED_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated?";
    public static final String API_KEY_PARAM = "api_key";



    public static final String CREDITS_QUERY = "/credits?";
    public static final String VIDEOS_QUERY = "/videos?";
    public static final String REVIEWS_QUERY = "/reviews?";

    public static final String LOG_TAG = "Popular Movies";

    public static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_THUMBNAIL_QUALITY = "/0.jpg";

    public static final String YOUTUBE_WEB = "http://www.youtube.com/watch?v=";


    public static final String LAST_DAY_UPDATED = "last_day_updated";


    public static boolean isDeviceConnected(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static int getDayOfYear(){
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }


    // This handles the frequency of automatic updates
    public static boolean shouldSectionBeUpdated(Activity activity, int section){

        boolean isUpdated = false;

        SharedPreferences preference = activity.getPreferences(Context.MODE_PRIVATE);

        if (preference.getInt(LAST_DAY_UPDATED + String.valueOf(section),0) == getDayOfYear()){
            isUpdated = true;
        }

        return isDeviceConnected(activity) && !isUpdated;
    }

    public static void saveUpdatedSectionDay(Activity activity, int section) {

        SharedPreferences preference = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(LAST_DAY_UPDATED + String.valueOf(section),getDayOfYear());
        editor.apply();

    }

    public static void downloadImageIntoFile(final Context context,
                                             final String filename, String url) {

        final Target targetBackdrop = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileOutputStream outputStream;
                        try {
                            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).run();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url)
                .into(targetBackdrop);
    }



    public static String getVideosString (MovieVideos[] movieVideos){
        String videoString = "";
        if (movieVideos!= null){
            for (MovieVideos videos:movieVideos){

                videoString += String.format("%-20.20s%s\n",videos.getName(),
                        ": " + PMUtility.YOUTUBE_WEB + videos.getKey());
            }
        }

        return videoString;

    }
}
