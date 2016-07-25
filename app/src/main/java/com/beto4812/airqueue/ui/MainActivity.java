package com.beto4812.airqueue.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.aws.AWSDynamoDbManager;
import com.beto4812.airqueue.aws.AWSIdentityManager;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.ui.main.settings.AboutFragment;
import com.beto4812.airqueue.ui.main.settings.SettingsFragment;
import com.beto4812.airqueue.utils.CircleTransform;
import com.beto4812.airqueue.utils.GetDataNew;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MainActivity";

    private static AWSDynamoDbManager dynamoDbManager = null;

    private FragmentManager mFragmentManager;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private TextView textUserName;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchUserIdentity();

        this.dynamoDbManager = AWSClientManager.defaultMobileClient().getDynamoDbManager();
        //new GetReadings().execute(); //This to retrieve AWS SensorReadings

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        textUserName = (TextView) headerView.findViewById(R.id.textUserName);
        profilePicture = (ImageView) headerView.findViewById(R.id.imageProfilePicture);

        //textUserName.setText(getString(R.string.text_full_name, userFirstName, userLastName));

        //Log.v(LOG_TAG, "onCreate() rendering profile picture");
        Picasso.with(MainActivity.this).load(R.drawable.img_logo_white).transform(new CircleTransform()).into(profilePicture);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.frame_content, new VisualizationsFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                mFragmentManager.beginTransaction().replace(R.id.frame_content, new VisualizationsFragment(), "VISUALIZATIONS").commit();
                break;
            case R.id.nav_settings:
                mFragmentManager.beginTransaction().replace(R.id.frame_content, SettingsFragment.newInstance()).commit();
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
        //Log.d(LOG_TAG, "fetchUserIdentity");

        AWSClientManager.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new AWSIdentityManager.IdentityHandler() {

                    @Override
                    public void handleIdentityID(String identityId) {
                        Log.d(LOG_TAG, "handleIdentityID(): " + identityId);
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.v(LOG_TAG, Log.getStackTraceString(exception));
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.frame_content);

        Log.v(LOG_TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            //case R.id.action_settings:
            //    Log.v(LOG_TAG, "nav_settings");
            //    mFragmentManager.beginTransaction().replace(R.id.frame_content, SettingsFragment.newInstance(), "TAG").commit();
            //    break;
            //case R.id.action_author:
            //    Log.v(LOG_TAG, "nav_author");
            //    mFragmentManager.beginTransaction().replace(R.id.frame_content, AboutFragment.newInstance()).commit();
            //    break;
            /*case R.id.action_demo:
                if(fragment instanceof VisualizationsFragment){
                    VisualizationsFragment visualizationsFragment = (VisualizationsFragment)fragment;
                    //Log.v(LOG_TAG, "getCurrentItem: " + visualizationsFragment.getViewPager().getCurrentItem());
                    Fragment currentFragment = visualizationsFragment.getChildFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":"+visualizationsFragment.getViewPager().getCurrentItem());
                    //Log.v(LOG_TAG, "currentFragment: " + currentFragment + " tag: "  +"android:switcher:"+R.id.viewpager+":"+visualizationsFragment.getViewPager().getCurrentItem());
                    GetDataNew.setSimulated(!GetDataNew.isSimulated());
                    GetDataNew.executeGetData(this, (GetDataNew.OnDataReceivedListener) currentFragment);
                    //Log.v(LOG_TAG, "currentFragmentContext: " + visualizationsFragment.getContext());
                    if(GetDataNew.isSimulated()){
                        Snackbar.make(visualizationsFragment.getView(), "Simulated mode activated", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(visualizationsFragment.getView(), "Deactivated simulated mode", Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar.make(fragment.getView(), "Not allowed here", Snackbar.LENGTH_SHORT).show();
                }
                break;*/
            case R.id.wipe_data:
                PreferenceManager.getDefaultSharedPreferences(fragment.getContext()).edit().clear().commit();
                Snackbar.make(fragment.getView(), "Data erased from device", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
