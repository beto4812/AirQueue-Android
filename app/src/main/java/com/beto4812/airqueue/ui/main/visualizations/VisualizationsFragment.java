package com.beto4812.airqueue.ui.main.visualizations;

import android.os.Bundle;
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
import com.beto4812.airqueue.ui.main.home.charts.PieChartsFragment;

public class VisualizationsFragment extends Fragment {

    private static final String LOG_TAG = "AWSClientManager";

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
                    return CircularVisualizationFragment.newInstance();
                case 1:
                    return LinearVisualizationFragment.newInstance();
                case 2:
                    return PieChartsFragment.newInstance();
                default:
                    return CircularVisualizationFragment.newInstance();
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
}
