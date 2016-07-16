package com.example.orobles.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by oroblesr on 5/11/16.
 */
public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.ViewHolder> {

    private Context mContext;
    private MovieDetails mMovieDetails;
    private boolean removeItems = true;

    public MovieCastAdapter(Context context) {
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_cast,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mMovieDetails != null && mMovieDetails.getMovieCredits().length > 0){
            MovieCredits credit = mMovieDetails.getMovieCredits()[position];
            holder.character.setText(credit.getCharacter());
            holder.castName.setText(credit.getName());
            Picasso.with(mContext)
                    .load(credit.getProfile_path_url())
                    .placeholder(R.drawable.perm_group_sync_settings)
                    .error(R.drawable.ic_perm_group_personal_info)
                    .into(holder.imageCharacter);
            holder.imageCharacter.setContentDescription(credit.getCharacter());
        }
        else  if (PMUtility.isDeviceConnected(mContext)){
            //Cast is still downloading
            holder.character.setText(R.string.still_loading);
        }
        else {
            holder.character.setText(R.string.connect_to_network);
        }

    }

    public void toggleItems() {
        this.removeItems = !this.removeItems;
    }


    @Override
    public int getItemCount() {
        if (removeItems) {
            return 0;
        }
        if (mMovieDetails != null){
            return mMovieDetails.getMovieCredits().length;
        }

        // Return 1 to display a message when cast is not yet available
        return 1;
    }

    public void setMovieDetailsArray(MovieDetails mMovieDetails) {
        this.mMovieDetails = mMovieDetails;

    }

    public MovieDetails getMovieDetails() {
        return mMovieDetails;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView character;
        public TextView castName;
        public ImageView imageCharacter;

        public ViewHolder(View mView) {
            super(mView);
            character = (TextView) mView.findViewById(R.id.list_character_name);
            castName = (TextView) mView.findViewById(R.id.list_cast_name);
            imageCharacter = (ImageView) mView.findViewById(R.id.list_image_character);
        }
    }
}

