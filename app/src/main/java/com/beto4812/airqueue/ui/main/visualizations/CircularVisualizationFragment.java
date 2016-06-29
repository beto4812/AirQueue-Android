package com.beto4812.airqueue.ui.main.visualizations;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.SensorCoordinates;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.visualizations.viewAdapter.CircularVisualizationAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CircularVisualizationFragment extends Fragment {

    private static final String LOG_TAG = "PollutantsCCF2";


    private RecyclerView recyclerView;

    private View rootView;
    private LinearLayout linearLayout;
    private FrameLayout.LayoutParams params;
    public SharedPreferences sharedPreferences;
    public GridLayoutManager layoutManager;
    public CircularVisualizationAdapter circularVisualizationsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_pollutants_circular_view, container, false);
        initRecyclerView();
        new GetAndAddSensorReadings().execute();
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
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
    }

    public static CircularVisualizationFragment newInstance() {
        Log.v(LOG_TAG, "newInstance");
        return new CircularVisualizationFragment();
    }

    private class GetAndAddSensorReadings extends AsyncTask<Void, Void, SensorReading> {
        //@Override
        protected void onPostExecute(SensorReading s) {
            Log.v(LOG_TAG, "onPostExecute() closestSensorID: " + s.getSourceID() + " lastUpdated: " + s.getLastUpdated());
            Iterator it = s.getAvailablePollutants().iterator();
            List<Object> renderList = new ArrayList<>();
            Map<Integer, List<Pollutant>> pollutantsByCategory= new TreeMap<>();
            Pollutant p;
            while(it.hasNext()){
                p = (Pollutant)it.next();
                //list.add(p);
                if(!pollutantsByCategory.containsKey(p.getCategory())){
                    pollutantsByCategory.put(p.getCategory(), new ArrayList<Pollutant>());
                }
                pollutantsByCategory.get(p.getCategory()).add(p);
            }

            Set<Integer> keys = pollutantsByCategory.keySet();
            List<Pollutant> temp;
            Iterator itTemp = null;

            for(int k: keys){
                Log.v(LOG_TAG, "k: " + k);
                renderList.add(new PollutantCategoryInfo(k));
                temp = pollutantsByCategory.get(k);
                itTemp = temp.iterator();
                while (itTemp.hasNext()){
                    renderList.add(itTemp.next());
                }
            }

            circularVisualizationsAdapter = new CircularVisualizationAdapter(renderList);
            recyclerView.setAdapter(circularVisualizationsAdapter);
        }

        @Override
        protected SensorReading doInBackground(Void... p) {

            double currentLat = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLatitude","55.9449353"));
            double currentLong = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLongitude","-3.1839465"));

            SensorCoordinates sensorCoordinates = AWSClientManager.defaultMobileClient().getDynamoDbManager().getClosestSensorCoordinates(currentLat, currentLong);
            SensorReading sensorReading = AWSClientManager.defaultMobileClient().getDynamoDbManager().getLastSensorReadingBySourceID(sensorCoordinates.getSourceID());

            return sensorReading;
        }
    }

}
