package com.example.orobles.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by oroblesr on 6/21/16.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private Context mContext;
    private MovieDetails mMovieDetails;
    private boolean removeItems = true;

    public MovieReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setMovieDetailsArray(MovieDetails mMovieDetails) {
        this.mMovieDetails = mMovieDetails;
    }

    public MovieDetails getMovieDetails() {
        return mMovieDetails;
    }

    public void toggleItems() {
        this.removeItems = !this.removeItems;
    }


    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_review,parent,false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.ViewHolder holder, int position) {
        String reviewTitle;

        if (mMovieDetails != null && mMovieDetails.getMovieReviews().length > 0){
            reviewTitle = mContext.getResources().getString(R.string.review_by_string)
                    + " " + mMovieDetails.getMovieReviews()[position].getAuthor() ;
            holder.review.setText(mMovieDetails.getMovieReviews()[position].getContent());
        }
        else  if (PMUtility.isDeviceConnected(mContext)){
            reviewTitle = mContext.getResources().getString(R.string.no_review_available);
        }
        else {
            reviewTitle = mContext.getResources().getString(R.string.connect_to_network);
        }
        holder.reviewerName.setText(reviewTitle);
    }

    @Override
    public int getItemCount() {
        if (removeItems) {
            return 0;
        }
        if (mMovieDetails != null) {
            if (mMovieDetails.getMovieReviews().length > 0) {
                return mMovieDetails.getMovieReviews().length;
            }
        }
        // Return 1 to display a message when reviews are not available
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView reviewerName;
        public TextView review;

        public ViewHolder(View mView) {
            super(mView);
            reviewerName = (TextView) mView.findViewById(R.id.reviewer_name);
            review = (TextView) mView.findViewById(R.id.review_text);
        }
    }
}
