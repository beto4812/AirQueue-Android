package com.beto4812.airqueue.ui.register;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.ui.BaseActivity;

import butterknife.Bind;

public class BaseDrawerActivity extends BaseActivity{

    private static final String LOG_TAG = "BaseDrawerActivity";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView vNavigation;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        bindViews();
        setup();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setup(){
        Log.v(LOG_TAG, "setup()");
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        vNavigation.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Log.v("avazquez", "onNavigationItemSelected: " + item);
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();

                        if (id == R.id.nav_log_out) {
                            //logout();
                            Log.v("avazquez", "R.id.nav_log_out selected");
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }

        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
}
