package com.beto4812.airqueue.ui;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.aws.AWSDynamoDbManager;
import com.beto4812.airqueue.aws.AWSIdentityManager;
import com.beto4812.airqueue.ui.main.home.HomeFragment;
import com.beto4812.airqueue.ui.main.BaseDrawerActivity;
import com.beto4812.airqueue.ui.main.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "AWSClientManager";

    private static AWSDynamoDbManager dynamoDbManager = null;
    
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchUserIdentity();

        this.dynamoDbManager = AWSClientManager.defaultMobileClient().getDynamoDbManager();
        //new GetReadings().execute(); //This to retrieve AWS SensorReadings

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.frame_content, HomeFragment.newInstance()).commit();

        /*ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTest1(), "ONE");
        adapter.addFragment(new FragmentTest1(), "TWO");
        adapter.addFragment(new FragmentTest1(), "THREE");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);*/
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                Log.i("NSAN", "Home");
                mFragmentManager.beginTransaction().replace(R.id.frame_content, HomeFragment.newInstance()).commit();
                break;
            case R.id.nav_settings:
                Log.i("NSAN", "Settings");
                mFragmentManager.beginTransaction().replace(R.id.frame_content, SettingsFragment.newInstance()).commit();
                break;
            case R.id.nav_log_out:
                Log.i("NSAN", "Log out");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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