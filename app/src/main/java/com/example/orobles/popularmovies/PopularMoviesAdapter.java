package com.example.orobles.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by oroblesr on 5/3/16.
 */
public class PopularMoviesAdapter extends BaseAdapter {
    private Context mContext;
    private PopularMoviesParcelable[] mPMArray;
    private final String PM_KEY = "ParcelableArray";
    private final String SECTION_KEY = "section";
    private Fragment mFragment;
    private int section;

    private final int POPULAR_SECTION = 0;
    private final int TOP_RATED_SECTION = 1;
    private final int FAVORITES_SECTION = 2;

    private final int TOTAL_VIEWS = 1;
    private final int PM_VIEW = 0;

    public PopularMoviesAdapter(Fragment fragment, int section){
        this.mFragment = fragment;
        this.mContext = fragment.getActivity();
        this.section = section;
    }


    @Override
    public int getCount() {
        if (mPMArray != null){
            return mPMArray.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount(){
        return TOTAL_VIEWS;
    }
    @Override
    public int getItemViewType (int position){
        return PM_VIEW;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        final ImageView posterImageView;

        if ( type == PM_VIEW && convertView == null){
            posterImageView = new ImageView(mContext);
            // Get rid of the white spaces
            posterImageView.setAdjustViewBounds(true);
            posterImageView.setId(position);
        } else {
            posterImageView = (ImageView) convertView;
        }

        final String filename = mPMArray[position].getPoster_filename();
        final File file = new File(mContext.getFilesDir(), filename);


        if (file.exists()){
            Picasso.with(mContext)
                    .load(file)
                    .placeholder(R.drawable.perm_group_sync_settings)
                    .error(R.drawable.ic_perm_group_personal_info)
                    .into(posterImageView);

        }
        else {
            downloadImageIntoTarget(posterImageView,position);

        }

        final String filenameBackdrop = mPMArray[position].getBackdrop_filename();
        final File fileBackdrop = new File(mContext.getFilesDir(), filenameBackdrop);

        if (!fileBackdrop.exists()) {
            PMUtility.downloadImageIntoFile(mContext,
                    filenameBackdrop,mPMArray[position].getBackdrop_path_url());
        }

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isTablet = mContext.getResources().getBoolean(R.bool.isTablet);
                if (isTablet){
                    openTabletFragment(position);

                }
                else {
                    Intent intent = new Intent(mContext, PMDetailActivity.class);
                    intent.putExtra(PM_KEY, mPMArray[position]);
                    mContext.startActivity(intent);
                }

            }
        });

        return posterImageView;


    }

    private void downloadImageIntoTarget(final ImageView posterImageView, int position){

        final String filename = mPMArray[position].getPoster_filename();

        final Target targetPoster = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                posterImageView.setImageBitmap(bitmap);

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        FileOutputStream outputStream;
                        try {
                            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        posterImageView.setTag(targetPoster);
        Picasso.with(mContext)
                .load(mPMArray[position].getPoster_path_url())
                .placeholder(R.drawable.perm_group_sync_settings)
                .error(R.drawable.ic_perm_group_personal_info)
                .into(targetPoster);
    }


    public void setMPopularMoviesParcelableArray(
            PopularMoviesParcelable[] popularMoviesParcelableArray){
        mPMArray = popularMoviesParcelableArray;

    }


    public void openTabletFragment(int position){
        //Set mPopularMoviesParcelableArray to MainActivity
        Intent intent = new Intent();
        intent.putExtra(PM_KEY, mPMArray[position]);
        intent.putExtra(SECTION_KEY, section);

        ((AppCompatActivity) mContext).setIntent(intent);

        mFragment.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container,  new PMDetailFragment())
                .commit();
    }



    public PopularMoviesParcelable[] getPMArray() {
        return mPMArray;
    }


}
