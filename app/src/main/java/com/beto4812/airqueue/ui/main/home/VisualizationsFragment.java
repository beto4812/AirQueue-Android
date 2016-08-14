package com.beto4812.airqueue.ui.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.ui.main.overview.viewHolder.OverviewFragment;
import com.beto4812.airqueue.ui.main.pollutantsCircular.CircularVisualizationFragment;
import com.beto4812.airqueue.ui.main.pollutantsLinear.LinearVisualizationFragment;

public class VisualizationsFragment extends Fragment {

    private static final String LOG_TAG = "VisualizationsFragment";
    private View rootView;
    private ViewPagerAdapter mPagerAdapter;
    public static ViewPager pager;

    public static VisualizationsFragment newInstance(){
        return new VisualizationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_home, container, false);

        pager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.home_tabs);
        pager.setAdapter(mPagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float v, final int i2) {
            }

            @Override
            public void onPageSelected(final int position) {
                FragmentVisibleInterface fragment = (FragmentVisibleInterface) mPagerAdapter.instantiateItem(pager, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int position) {
            }
        });
        tabLayout.setupWithViewPager(pager);

        //getData.execute();
        return rootView;
    }

    public interface FragmentVisibleInterface {
        void fragmentBecameVisible();
    }

    public ViewPager getViewPager(){
        return pager;
    }

    public FragmentPagerAdapter getFragmentPageAdapter(){
        return mPagerAdapter;
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
                    OverviewFragment overviewFragment = OverviewFragment.newInstance();
                    //getData.setListener(overviewFragment);
                    return overviewFragment;
                case 1:
                    CircularVisualizationFragment circularVisualizationFragment = CircularVisualizationFragment.newInstance();
                    //getData.setListener(circularVisualizationFragment);
                    return circularVisualizationFragment;
                case 2:
                    LinearVisualizationFragment linearVisualizationFragment = LinearVisualizationFragment.newInstance();
                    //getData.setListener(linearVisualizationFragment);
                    return linearVisualizationFragment;
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
