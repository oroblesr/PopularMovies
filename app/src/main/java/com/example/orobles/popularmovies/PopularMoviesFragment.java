package com.example.orobles.popularmovies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orobles.popularmovies.data.MoviesProvider;


public class PopularMoviesFragment extends Fragment {

    private PopularMoviesAdapter mPMAdapter;
    private final String SECTION = "Section";
    private int section;
    private FetchPopularMoviesTask mPMTask;
    private Bundle arguments;

    private final int POPULAR_SECTION = 0;
    private final int TOP_RATED_SECTION = 1;
    private final int FAVORITES_SECTION = 2;
    private FavoriteContentObserver favoriteContentObserver;

    private Handler handler = new Handler();

    class FavoriteContentObserver extends ContentObserver {
        public FavoriteContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            setRetainInstance(true);
            boolean isTablet = getContext().getResources().getBoolean(R.bool.isTablet);
            if (isTablet && section == FAVORITES_SECTION && mPMAdapter != null ){
                DBOperations dbOperations = new DBOperations();
                dbOperations.getFavoriteMoviesInDB(getContext(),mPMAdapter);
                dbOperations.execute();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
        section = arguments.getInt(SECTION);
        mPMAdapter = new PopularMoviesAdapter (this, section);
        setHasOptionsMenu(true);
        ContentResolver cr = getContext().getContentResolver();
        favoriteContentObserver = new FavoriteContentObserver(handler);
        cr.registerContentObserver(MoviesProvider.Movies.MOVIES,true,favoriteContentObserver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (PMUtility.isDeviceConnected(getActivity())){
                if (mPMTask.getStatus().equals(AsyncTask.Status.FINISHED) ||
                        mPMTask.getStatus().equals(AsyncTask.Status.PENDING)){
                    mPMTask = new FetchPopularMoviesTask(getActivity(),mPMAdapter);
                    mPMTask.execute(arguments.getInt(SECTION));
                }
                else {
                    Toast toast = Toast.makeText(getActivity(),
                            getString(R.string.refresh_is_running), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else {
                Toast toast = Toast.makeText(getActivity(),
                        getString(R.string.connect_to_network), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        GridView gridview = (GridView) view.findViewById(R.id.gridview_popular_movies);
        gridview.setAdapter(mPMAdapter);

        mPMTask = new FetchPopularMoviesTask(getActivity(),mPMAdapter);



        TextView emptyText = (TextView) view.findViewById(R.id.emptyFavorites);
        if (section == FAVORITES_SECTION){
            emptyText.setText(R.string.no_favorites);
        }
        else if (PMUtility.isDeviceConnected(getContext())) {
            emptyText.setText(R.string.still_loading);
        }
        else {
            emptyText.setText(R.string.connect_to_network);
        }
        gridview.setEmptyView(emptyText);

        if (section != FAVORITES_SECTION &&
                PMUtility.shouldSectionBeUpdated(getActivity(),section)){
            mPMTask.execute(section);
        }
        else {
            DBOperations dbOperations = new DBOperations();
            if (section == POPULAR_SECTION) {
                dbOperations.getPopularMoviesInDB(getContext(),mPMAdapter);
                dbOperations.execute();
            }
            else if (section == TOP_RATED_SECTION){
                dbOperations.getTopRatedMoviesInDB(getContext(),mPMAdapter);
                dbOperations.execute();
            }
            else if (section == FAVORITES_SECTION){
                dbOperations.getFavoriteMoviesInDB(getContext(),mPMAdapter);
                dbOperations.execute();
            }

            if (!PMUtility.isDeviceConnected(getContext())){
                Toast toast = Toast.makeText(getActivity(),
                        getString(R.string.connect_to_network), Toast.LENGTH_LONG);
                toast.show();
            }

        }

    }



    @Override
    public void onResume() {
        super.onResume();

        int section = arguments.getInt(SECTION);
        DBOperations dbOperations = new DBOperations();

        if (section == FAVORITES_SECTION && mPMAdapter != null){
            dbOperations.getFavoriteMoviesInDB(getContext(),mPMAdapter);
            dbOperations.execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( favoriteContentObserver != null ) {
            ContentResolver cr = getContext().getContentResolver();
            cr.unregisterContentObserver( favoriteContentObserver );
        }
    }


}

