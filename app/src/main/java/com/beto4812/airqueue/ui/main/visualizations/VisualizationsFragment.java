package com.beto4812.airqueue.ui.main.visualizations;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorCoordinates;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.visualizations.viewHolder.OverviewFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VisualizationsFragment extends Fragment {

    private static final String LOG_TAG = "VisualizationsFragment";

    public VisualizationsFragment() {

    }

    public static VisualizationsFragment newInstance() {
        VisualizationsFragment fragment = new VisualizationsFragment();
        Bundle args = new Bundle();
        //Pass extra arguments if needed
        //args.putString(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetData().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.home_tabs);

        pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(pager);

        return rootView;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        String[] tabsArray;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            tabsArray = getContext().getResources().getStringArray(R.array.vis_tabs_titles);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OverviewFragment.getInstance();
                case 1:
                    return CircularVisualizationFragment.getInstance();
                case 2:
                    return LinearVisualizationFragment.getInstance();
                default:
                    return CircularVisualizationFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return tabsArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsArray[position];
        }
    }

    private class GetData extends AsyncTask<Void, Void, List<SensorReading>> {
        //@Override
        protected void onPostExecute(List<SensorReading> s) {
            Log.v(LOG_TAG, "onPostExecute: " + s.toString());
            if(s.size()>1){
                OverviewFragment.getInstance().setSensorReading(s.get(s.size()-1));
                CircularVisualizationFragment.getInstance().setSensorReading(s.get(s.size()-1));
            }
            LinearVisualizationFragment.getInstance().setreadings(s);
        }

        @Override
        protected List<SensorReading> doInBackground(Void... p) {

            double currentLat = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLatitude","55.9449353"));
            double currentLong = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLongitude","-3.1839465"));

            SensorCoordinates sensorCoordinates = AWSClientManager.defaultMobileClient().getDynamoDbManager().getClosestSensorCoordinates(currentLat, currentLong);
            HashMap<String, PollutantThreshold> pollutantThresholds = AWSClientManager.defaultMobileClient().getDynamoDbManager().getPollutantThresholds();
            CircularVisualizationFragment.getInstance().setPollutantThresholds(pollutantThresholds);

            Calendar c = Calendar.getInstance();
            Date from = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            Date to = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String toS = dateFormatter.format(to);
            String fromS = dateFormatter.format(from);


            List<SensorReading> sensorReadings = AWSClientManager.defaultMobileClient().getDynamoDbManager().
                    getReadingsBySourceID(sensorCoordinates.getSourceID(), fromS, toS);


            return sensorReadings;
        }
    }
}
