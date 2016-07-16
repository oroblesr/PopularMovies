package com.example.orobles.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oroblesr on 5/4/16.
 */
public class PMDetailActivity extends AppCompatActivity {

    private String intentKey = "ParcelableArray";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

       if (savedInstanceState == null) {
            PMDetailFragment mPMDetailFragment = new PMDetailFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, mPMDetailFragment)
                    .commit();
        }


    }




}




