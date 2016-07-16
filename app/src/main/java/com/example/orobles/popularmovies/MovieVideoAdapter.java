package com.example.orobles.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by oroblesr on 5/20/16.
 */
public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.ViewHolder>  {

    private Context mContext;
    private MovieDetails mMovieDetails;
    private boolean removeItems = true;

    public MovieVideoAdapter(Context context) {
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_video,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mMovieDetails != null && mMovieDetails.getMovieVideos().length > 0) {

            final MovieVideos video = mMovieDetails.getMovieVideos()[position];
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" +
                                video.getKey()));
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(PMUtility.YOUTUBE_WEB + video.getKey()));
                        mContext.startActivity(intent);
                    }

                }
            });
            holder.title.setText(video.getName());
            if (PMUtility.isDeviceConnected(mContext)) {
                Picasso.with(mContext)
                        .load(video.getThumbnailURL())
                        .placeholder(R.drawable.perm_group_sync_settings)
                        .error(R.drawable.ic_perm_group_personal_info)
                        .into(holder.videoThumbnail);
                holder.videoThumbnail.setContentDescription(video.getName());
            }

        }
        else if (PMUtility.isDeviceConnected(mContext)) {
            //Videos are still downloading
            holder.title.setText(R.string.still_loading);
        }
        else {
            holder.title.setText(R.string.connect_to_network);
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
            return mMovieDetails.getMovieVideos().length;
        }

        // Return 1 to display a message when video is not yet available
        return 1;
    }

    public void setMovieDetailsArray(MovieDetails mMovieDetails) {
        this.mMovieDetails = mMovieDetails;
    }

    public MovieDetails getMovieDetails() {
        return mMovieDetails;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView videoThumbnail;


        public ViewHolder(View mView) {
            super(mView);
            title = (TextView) mView.findViewById(R.id.video_name);
            videoThumbnail = (ImageView) mView.findViewById(R.id.video_thumbnail);
        }


    }
}
