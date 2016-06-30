package com.beto4812.airqueue.ui.main.visualizations;

import android.os.Bundle;
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
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.visualizations.viewAdapter.LinearVisualizationAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
    private static LinearVisualizationFragment instance;
    private List<SensorReading> readings;

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
        if(readings!=null){
            updateUI();
        }
        return rootView;
    }

    private void initRecyclerView() {
        Log.v(LOG_TAG, "initRecyclerView()");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pollutants_linear_recycler);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
    }

    public static LinearVisualizationFragment getInstance(){
        if(instance==null){
            instance =  new LinearVisualizationFragment();
        }
        return instance;
    }

    public void setreadings(List<SensorReading> readings){
        this.readings = readings;
        if(rootView!=null){
            updateUI();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void updateUI(){
        Collection pollutantCodes = Pollutant.allPollutants().keySet();
        String values[] = new String[pollutantCodes.size()];
        List<Object> renderList = new ArrayList<>();

        Iterator it = pollutantCodes.iterator();

        for(int i = 0; it.hasNext(); i++){
            values[i] = (String)it.next();
            Log.v(LOG_TAG, "setupUI "+i + " " + values[i]);
            renderList.add(new SensorPollutantReadings(readings, values[i]));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        linearVisualizationAdapter = new LinearVisualizationAdapter(renderList);
        recyclerView.setAdapter(linearVisualizationAdapter);
    }
}
