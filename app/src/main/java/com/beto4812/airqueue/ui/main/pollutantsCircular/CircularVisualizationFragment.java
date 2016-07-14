package com.beto4812.airqueue.ui.main.pollutantsCircular;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.ui.main.pollutantsCircular.viewAdapter.CircularVisualizationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CircularVisualizationFragment extends Fragment implements VisualizationsFragment.FragmentVisibleInterface {

    private static final String LOG_TAG = "CircularVisualizationF";


    private RecyclerView recyclerView;

    private View rootView;
    public GridLayoutManager layoutManager;
    public CircularVisualizationAdapter circularVisualizationsAdapter;
    private static CircularVisualizationFragment instance;
    private SensorReading sensorReading;
    private HashMap<String, PollutantThreshold> pollutantThresholds;
    private HashMap<String, PollutantCategoryInfo> pollutantCategoryInfo;
    private TextView textViewDescription;
    private RelativeLayout headerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_pollutants_circular_view, container, false);
        headerLayout = (RelativeLayout)rootView.findViewById(R.id.circular_view_header_relative_layout);

        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");
        ((TextView) rootView.findViewById(R.id.textViewDescription)).setTypeface(openSansBold);
        textViewDescription = (TextView)rootView.findViewById(R.id.textViewDescription);

        initRecyclerView();
        if(sensorReading!=null){
            updateUI();
        }
        return rootView;
    }

    private void initRecyclerView() {
        Log.v(LOG_TAG, "initRecyclerView()");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pollutants_circular_recycler);

        layoutManager = new GridLayoutManager(getContext(), 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(circularVisualizationsAdapter.getItemViewType(position)){
                    case CircularVisualizationAdapter.POLLUTANT_INFO:
                        return 2;
                    case CircularVisualizationAdapter.POLLUTANT_VIEW:
                        if(circularVisualizationsAdapter.getPollutant(position).getCategory()==Pollutant.PollutantCategory.PARTICULATE_MATTER){
                            return 1;
                        }else{
                            return 2;
                        }
                    default:
                        return -1;
                }
            }
        });

        recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener(){

            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                    headerLayout.animate().translationY(-headerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    headerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                    //onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }
            }

        });
        recyclerView.setLayoutManager(layoutManager);
    }

    public static CircularVisualizationFragment getInstance(){
        if(instance==null){
            instance =  new CircularVisualizationFragment();
        }
        return instance;
    }

    public void setSensorReading(SensorReading s){
        this.sensorReading = s;
        if(rootView!=null){
            updateUI();
        }
    }

    public void setPollutantThresholds(HashMap<String, PollutantThreshold> pollutantThresholds){
        this.pollutantThresholds = pollutantThresholds;
    }

    public void setPollutantCategoryInfo(HashMap<String, PollutantCategoryInfo> pollutantCategoryInfo){
        this.pollutantCategoryInfo = pollutantCategoryInfo;
    }

    public void updateUI(){
        Log.v(LOG_TAG, "onPostExecute() closestSensorID: " + sensorReading.getSourceID() + " lastUpdated: " + sensorReading.getLastUpdated());
        Iterator it = sensorReading.getAvailablePollutants().iterator();
        List<Object> renderList = new ArrayList<>();
        Map<Integer, List<Pollutant>> pollutantsByCategory= new TreeMap<>();
        Pollutant p;
        while(it.hasNext()){
            p = (Pollutant)it.next();
            if(pollutantThresholds!=null){
                p.setThreshold(pollutantThresholds.get(p.getCode()));
            }
            //list.add(p);
            if(!pollutantsByCategory.containsKey(p.getCategory())){
                pollutantsByCategory.put(p.getCategory(), new ArrayList<Pollutant>());
            }
            pollutantsByCategory.get(p.getCategory()).add(p);
        }

        Set<Integer> keys = pollutantsByCategory.keySet();
        List<Pollutant> temp;
        Iterator individualPollutantsIterator = null;
        Pollutant individualPollutant = null;

        for(int k: keys){
            Log.v(LOG_TAG, "pollutantCategoryInt: " + k);
            (pollutantCategoryInfo.get(Pollutant.PollutantCategory.getPollutantCategoryString(k))).setPollutantCategory(k);
            renderList.add(pollutantCategoryInfo.get(Pollutant.PollutantCategory.getPollutantCategoryString(k)));
            temp = pollutantsByCategory.get(k);
            individualPollutantsIterator = temp.iterator();
            while (individualPollutantsIterator.hasNext()){
                individualPollutant = (Pollutant)individualPollutantsIterator.next();
                Log.v(LOG_TAG, "individual pollutant: " + individualPollutant);
                if(individualPollutant.isRendereable()){
                    renderList.add(individualPollutant);
                }
            }
        }

        circularVisualizationsAdapter = new CircularVisualizationAdapter(renderList);
        recyclerView.setAdapter(circularVisualizationsAdapter);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
