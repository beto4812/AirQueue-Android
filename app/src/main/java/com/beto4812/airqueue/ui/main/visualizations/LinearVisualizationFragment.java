package com.beto4812.airqueue.ui.main.visualizations;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorCoordinates;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.visualizations.viewAdapter.LinearVisualizationAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class LinearVisualizationFragment extends Fragment {

    private static final String LOG_TAG = "LinearVisualizationFrag";

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private LinearVisualizationAdapter linearVisualizationAdapter;
    private View rootView;
    private Spinner spinner;
    private static Calendar c = Calendar.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_pollutants_linear_view, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.fragment_pollutants_linear_view_spinner);

        initRecyclerView();
        new GetAndAddSensorReadings().execute();
        return rootView;
    }

    private void initRecyclerView() {
        Log.v(LOG_TAG, "initRecyclerView()");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pollutants_linear_recycler);

        layoutManager = new GridLayoutManager(getContext(), 1);

        recyclerView.setLayoutManager(layoutManager);
    }

    public static LinearVisualizationFragment newInstance() {
        return new LinearVisualizationFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void setupUI(List<SensorReading> s){
        Collection pollutantCodes = Pollutant.allPollutants().keySet();
        String values[] = new String[pollutantCodes.size()];
        List<Object> renderList = new ArrayList<>();

        Iterator it = pollutantCodes.iterator();

        for(int i = 0; it.hasNext(); i++){
            values[i] = (String)it.next();
            Log.v(LOG_TAG, "setupUI "+i + " " + values[i]);
            renderList.add(new SensorPollutantReadings(s, values[i]));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        linearVisualizationAdapter = new LinearVisualizationAdapter(renderList);
        recyclerView.setAdapter(linearVisualizationAdapter);
    }

    private class GetAndAddSensorReadings extends AsyncTask<Void, Void, List<SensorReading>> {
        //@Override
        protected void onPostExecute(List<SensorReading> s) {
            setupUI(s);
        }

        @Override
        protected List<SensorReading> doInBackground(Void... p) {
            Log.v(LOG_TAG, "doInBackground");

            double currentLat = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLatitude","55.9449353"));
            double currentLong = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lastLongitude","-3.1839465"));

            Calendar c = Calendar.getInstance();
            Date from = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            Date to = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String toS = dateFormatter.format(to);
            String fromS = dateFormatter.format(from);


            SensorCoordinates sensorCoordinates = AWSClientManager.defaultMobileClient().getDynamoDbManager().
                    getClosestSensorCoordinates(currentLat, currentLong);
            List<SensorReading> sensorReadings = AWSClientManager.defaultMobileClient().getDynamoDbManager().
                    getReadingsBySourceID(sensorCoordinates.getSourceID(), fromS, toS);
            return sensorReadings;
        }
    }
}
