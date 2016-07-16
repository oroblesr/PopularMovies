package com.example.orobles.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    private final String SECTION = "Section";
    private final int NUMBER_OF_SECTIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StatePagerAdapter statePagerAdapter =
                new StatePagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(statePagerAdapter);
        //mViewPager.setOffscreenPageLimit(NUMBER_OF_SECTIONS);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        assert mTabLayout != null;
        mTabLayout.setupWithViewPager(mViewPager);

    }


    public class StatePagerAdapter extends FragmentPagerAdapter {

        public StatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int section) {
            Bundle arguments = new Bundle();
            arguments.putInt(SECTION,section);

            Fragment mFragment  = new PopularMoviesFragment();
            mFragment.setArguments(arguments);

            return mFragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_SECTIONS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.popular_tab_name);
                case 1:
                    return getString(R.string.top_rated_tab_name);
                case 2:
                    return getString(R.string.favorites_tab_name);
            }
            return null;
        }
    }

}
