package com.example.orobles.popularmovies;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orobles.popularmovies.data.MoviesProvider;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * Created by oroblesr on 5/24/16.
 */
public class PMDetailFragment extends Fragment {

    private String intentKey = "ParcelableArray";

    private CollapsingToolbarLayout collapsingToolbar;
    private TextView mFavoriteTextView;
    private TextView mTextViewTitle;
    private TextView mTextViewOverview;
    private TextView mTextViewReleaseDate;
    private ImageView mImageViewBackdrop;
    private ImageView mImageViewPoster;
    private RatingBar mRatingBarVotes;
    private RatingBar mFavoriteRatingBar;
    private Button mCastButton;
    private RecyclerView recyclerViewCast;
    private RecyclerView recyclerViewReview;
    private RecyclerView recyclerViewVideo;
    private Button mReviewButton;
    private Button mVideosButton;
    private MovieCastAdapter mMovieCastAdapter;
    private MovieVideoAdapter mMovieVideoAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;

    private FloatingActionButton fabShare;

    private String votes;
    private PopularMoviesParcelable moviesParcelable;
    private int section;
    private final String SECTION_KEY = "section";
    private final int FAVORITES_SECTION = 2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieCastAdapter = new MovieCastAdapter(getActivity());
        mMovieVideoAdapter = new MovieVideoAdapter(getActivity());
        mMovieReviewAdapter = new MovieReviewAdapter(getActivity());
        Intent mIntent = getActivity().getIntent();
        moviesParcelable = mIntent.getParcelableExtra(intentKey);
        section = mIntent.getIntExtra(SECTION_KEY,-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState){

        setRootView(rootView);
        int movieID = moviesParcelable.getId();

        votes = moviesParcelable.getVote_average() + "/10 ( " +
                Integer.toString(moviesParcelable.getVote_count()) + " votes )";



        if (moviesParcelable != null){
            // Set title of Detail page
            collapsingToolbar.setTitle(moviesParcelable.getOriginal_title());

            mTextViewReleaseDate.setText(moviesParcelable.getRelease_date().substring(0,4));
            mRatingBarVotes.setNumStars(5);
            mRatingBarVotes.setRating(Float.valueOf(moviesParcelable.getVote_average()) / 2);
            mTextViewTitle.setText(moviesParcelable.getOriginal_title());
            mTextViewOverview.setText(moviesParcelable.getOverview());


            String filenameBackdrop = moviesParcelable.getBackdrop_filename();
            File fileBackdrop = new File(getContext().getFilesDir(), filenameBackdrop);

            boolean isConnected = PMUtility.isDeviceConnected(getContext());
            if (!isConnected){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.connect_to_network),
                        Toast.LENGTH_LONG);
                toast.show();
            }

            if (fileBackdrop.exists()) {
                Picasso.with(getActivity())
                        .load(fileBackdrop)
                        .placeholder(R.drawable.perm_group_sync_settings)
                        .error(R.drawable.ic_perm_group_personal_info)
                        .into(mImageViewBackdrop);

                mImageViewBackdrop.setContentDescription(moviesParcelable.getOriginal_title());
            }

            // This is for handling and edge case when the user taps a Movie
            // while the backdrop is still downloading
            else if (isConnected){
                PMUtility.downloadImageIntoFile(getContext(),
                        filenameBackdrop, moviesParcelable.getBackdrop_path_url());
                Picasso.with(getActivity())
                        .load(moviesParcelable.getBackdrop_path_url())
                        .placeholder(R.drawable.perm_group_sync_settings)
                        .error(R.drawable.ic_perm_group_personal_info)
                        .into(mImageViewBackdrop);
                mImageViewBackdrop.setContentDescription(moviesParcelable.getOriginal_title());

            }


            String filenamePoster = moviesParcelable.getPoster_filename();
            File filePoster = new File(getContext().getFilesDir(), filenamePoster);

            if (filePoster.exists()){
                Picasso.with(getActivity())
                        .load(filePoster)
                        .placeholder(R.drawable.perm_group_sync_settings)
                        .error(R.drawable.ic_perm_group_personal_info)
                        .into(mImageViewPoster);
                mImageViewPoster.setContentDescription(moviesParcelable.getOriginal_title());
            }
            // This is for handling and edge case when the user taps a Movie
            // while the poster is still downloading
            else if (isConnected){
                PMUtility.downloadImageIntoFile(getContext(),
                        filenamePoster, moviesParcelable.getPoster_path_url());
                Picasso.with(getActivity())
                        .load(moviesParcelable.getPoster_path_url())
                        .placeholder(R.drawable.perm_group_sync_settings)
                        .error(R.drawable.ic_perm_group_personal_info)
                        .into(mImageViewPoster);
                mImageViewPoster.setContentDescription(moviesParcelable.getOriginal_title());
            }


            DBOperations dbOperations = new DBOperations();
            dbOperations.getMovieDetailsInDB(getContext(), moviesParcelable.getId(),
                    mMovieCastAdapter,mMovieReviewAdapter,mMovieVideoAdapter);
            dbOperations.execute();

            mRatingBarVotes.setOnTouchListener(ratingBarTouchListener);
            mFavoriteRatingBar.setOnTouchListener(favoriteTouchListener);
            mCastButton.setOnClickListener(castClickListener);
            mReviewButton.setOnClickListener(reviewClickListener);
            mVideosButton.setOnClickListener(videosClickListener);

            fabShare.setOnClickListener(fabShareListener);


        }
    }


    @Override
    public void onResume () {
        super.onResume();
        int movieID = moviesParcelable.getId();
        moviesParcelable.setIsFavorite(DBOperations.isFavoriteInDB(getContext(),movieID));
        setFavoriteStar();

    }

    @Override
    public void onPause (){
        super.onPause();
            if (moviesParcelable != null){
                boolean isFavorite = moviesParcelable.getIsFavorite() == PMUtility.INT_YES;
                boolean isTablet = getContext().getResources().getBoolean(R.bool.isTablet);
                if (isTablet && section == FAVORITES_SECTION && !isFavorite) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.remove(this);
                    fragmentTransaction.commit();
                }
            }
    }


    private void setRootView(View rootView){

        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        mTextViewReleaseDate = (TextView) rootView.findViewById(R.id.textViewReleaseDate);
        mRatingBarVotes = (RatingBar) rootView.findViewById(R.id.ratingBarVotes);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        mTextViewOverview = (TextView) rootView.findViewById(R.id.textViewOverview);
        mImageViewBackdrop = (ImageView) rootView.findViewById(R.id.imageBackdrop);
        mImageViewPoster = (ImageView) rootView.findViewById(R.id.imagePoster);
        mFavoriteRatingBar = (RatingBar) rootView.findViewById(R.id.favoriteRatingBar);
        mFavoriteTextView = (TextView) rootView.findViewById(R.id.favoriteTextView);

        mCastButton = (Button) rootView.findViewById(R.id.castButton);
        recyclerViewCast = (RecyclerView) rootView.findViewById(R.id.recyclerViewCast);
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCast.setHasFixedSize(false);

        mReviewButton = (Button) rootView.findViewById(R.id.reviewButton);
        recyclerViewReview = (RecyclerView) rootView.findViewById(R.id.recyclerViewReview);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewReview.setHasFixedSize(false);

        mVideosButton = (Button) rootView.findViewById(R.id.videoButton);
        recyclerViewVideo = (RecyclerView) rootView.findViewById(R.id.recyclerViewVideo);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewVideo.setHasFixedSize(false);

        fabShare = (FloatingActionButton) rootView.findViewById(R.id.fab_share);

        recyclerViewCast.setAdapter(mMovieCastAdapter);
        recyclerViewReview.setAdapter(mMovieReviewAdapter);
        recyclerViewVideo.setAdapter(mMovieVideoAdapter);


    }

    private void setFavoriteStar(){
        if (moviesParcelable.getIsFavorite() == PMUtility.INT_YES){
            mFavoriteTextView.setText(getResources().getString(R.string.unset_as_favorite_text));
            mFavoriteRatingBar.setRating(PMUtility.INT_YES);
        }
        else {
            mFavoriteRatingBar.setRating(PMUtility.INT_NO);
            mFavoriteTextView.setText(getResources().getString(R.string.set_as_favorite_text));
        }

    }


    private View.OnClickListener fabShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String videosToShare;
            if (mMovieVideoAdapter != null
                    && mMovieVideoAdapter.getMovieDetails() != null
                    && moviesParcelable != null ){
                MovieVideos[] movieVideos = mMovieVideoAdapter.getMovieDetails().getMovieVideos();
                videosToShare = moviesParcelable.getOriginal_title();
                videosToShare += " movie videos\n";
                videosToShare += PMUtility.getVideosString(movieVideos);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, videosToShare);
                sendIntent.setType("text/*");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.sharing_videos)));
            }
            else {
                Snackbar.make(view, R.string.still_loading, Snackbar.LENGTH_LONG).show();

            }

        }
    };

    private View.OnTouchListener ratingBarTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Snackbar.make(v, votes,
                        Snackbar.LENGTH_LONG).show();
            }

            return true;
        }
    };


    private View.OnClickListener videosClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMovieVideoAdapter.toggleItems();
            mMovieVideoAdapter.notifyDataSetChanged();
            if (!PMUtility.isDeviceConnected(getContext())) {
                Snackbar.make(v, R.string.connect_to_watch, Snackbar.LENGTH_LONG).show();
            }
        }
    };


    private View.OnClickListener castClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMovieCastAdapter.toggleItems();
            mMovieCastAdapter.notifyDataSetChanged();
        }
    };


    private View.OnTouchListener favoriteTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                DBOperations dbOperations = new DBOperations();

                if (moviesParcelable.getIsFavorite() == PMUtility.INT_YES){
                    mFavoriteRatingBar.setRating(PMUtility.INT_NO);
                    moviesParcelable.setIsFavorite(PMUtility.INT_NO);

                    dbOperations.removeMovieAsFavoriteInDB(getContext(), moviesParcelable.getId());
                    dbOperations.execute();

                    Snackbar.make(v, moviesParcelable.getOriginal_title() + " " +
                                    getString(R.string.unset_as_favorite_toast),
                            Snackbar.LENGTH_LONG).show();

                    mFavoriteTextView.setText(getResources().getString(R.string.set_as_favorite_text));





                }
                else {
                    mFavoriteRatingBar.setRating(PMUtility.INT_YES);
                    moviesParcelable.setIsFavorite(PMUtility.INT_YES);

                    Snackbar.make(v, moviesParcelable.getOriginal_title() + " " +
                                    getString(R.string.set_as_favorite_toast),
                            Snackbar.LENGTH_LONG).show();

                    dbOperations.addMovieAsFavoriteInDB(getContext(), moviesParcelable.getId());
                    dbOperations.execute();

                    mFavoriteTextView.setText(getResources().getString(R.string.unset_as_favorite_text));


                }

            }

            return true;
        }
    };


    private View.OnClickListener reviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMovieReviewAdapter.toggleItems();
            mMovieReviewAdapter.notifyDataSetChanged();
        }
    };



}