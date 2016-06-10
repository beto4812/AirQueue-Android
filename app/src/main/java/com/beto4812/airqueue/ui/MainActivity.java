package com.beto4812.airqueue.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.aws.AWSDynamoDbManager;
import com.beto4812.airqueue.aws.AWSIdentityManager;
import com.beto4812.airqueue.ui.register.BaseDrawerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseDrawerActivity {

    private static final String LOG_TAG = "AWSClientManager";

    private static AWSDynamoDbManager dynamoDbManager = null;

    @Bind(R.id.main_tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        fetchUserIdentity();

        this.dynamoDbManager = AWSClientManager.defaultMobileClient().getDynamoDbManager();
        //new GetReadings().execute(); //This to retrieve AWS SensorReadings


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTest1(), "ONE");
        adapter.addFragment(new FragmentTest1(), "TWO");
        adapter.addFragment(new FragmentTest1(), "THREE");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void fetchUserIdentity() {
        Log.d(LOG_TAG, "fetchUserIdentity");

        AWSClientManager.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new AWSIdentityManager.IdentityHandler() {

                    @Override
                    public void handleIdentityID(String identityId) {
                        Log.d(LOG_TAG, "handleIdentityID(): " + identityId);
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.v(LOG_TAG,  Log.getStackTraceString(exception));
                    }
                });
    }

    private class GetReadings extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            dynamoDbManager.getReadingsList();
            return null;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}