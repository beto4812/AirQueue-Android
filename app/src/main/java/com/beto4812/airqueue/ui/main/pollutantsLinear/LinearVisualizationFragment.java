package com.beto4812.airqueue.ui.main.pollutantsLinear;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.ui.main.pollutantsLinear.viewAdapter.LinearVisualizationAdapter;
import com.beto4812.airqueue.utils.DataSingelton;
import com.beto4812.airqueue.utils.GetDataNew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LinearVisualizationFragment extends Fragment implements VisualizationsFragment.FragmentVisibleInterface, GetDataNew.OnDataReceivedListener {

    private static final String LOG_TAG = "LinearVisualizationFrag";

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private LinearVisualizationAdapter linearVisualizationAdapter;
    private View rootView;
    private static Calendar c = Calendar.getInstance();
    private RelativeLayout headerLayout;
    private List<SensorReading> readings;
    private HashMap<String, PollutantThreshold> pollutantThresholds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_pollutants_linear_view, container, false);
        initRecyclerView();
        if(!DataSingelton.getInstance().isEmpty()){
            setPollutantThresholds(DataSingelton.getInstance().getPollutantThresholds());
            setreadings(DataSingelton.getInstance().getSensorReadings());
        }

        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        ((TextView) rootView.findViewById(R.id.textViewDescription)).setTypeface(openSansBold);

        headerLayout = (RelativeLayout)rootView.findViewById(R.id.header_relative_layout);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerLayout.animate().translationY(-headerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
        });

        if(DataSingelton.getInstance().isEmpty()){
            GetDataNew.executeGetData(getContext(), this);
        }else{
            onDataReady();
        }
        return rootView;
    }

    private void initRecyclerView() {
        Log.v(LOG_TAG, "initRecyclerView()");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pollutants_linear_recycler);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
    }

    public static LinearVisualizationFragment newInstance(){
        return new LinearVisualizationFragment();
    }

    public void setreadings(List<SensorReading> readings){
        this.readings = readings;
    }

    public void setPollutantThresholds(HashMap<String, PollutantThreshold> pollutantThresholds){
        this.pollutantThresholds = pollutantThresholds;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDataReady() {
        Log.v(LOG_TAG, "onDataReady");
        setPollutantThresholds(DataSingelton.getInstance().getPollutantThresholds());
        setreadings(DataSingelton.getInstance().getSensorReadings());
        if(rootView!=null){
            updateUI();
        }
    }


    public void updateUI(){
        List<Object> renderList = new ArrayList<>();
        renderList.add(new RenderedObject(readings, pollutantThresholds));

        linearVisualizationAdapter = new LinearVisualizationAdapter(renderList);
        recyclerView.setAdapter(linearVisualizationAdapter);
    }

    @Override
    public void fragmentBecameVisible() {
        if(headerLayout!=null){
            headerLayout.setTranslationY(0);

            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Log.v(LOG_TAG, "finish");
                    headerLayout.animate().translationY(-headerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                }
            }.start();
        }
    }

    public class RenderedObject{
        private List<SensorReading> readings;
        private HashMap<String, PollutantThreshold> pollutantThresholds;

        public RenderedObject(List<SensorReading> readings, HashMap<String, PollutantThreshold> pollutantThresholds){
            this.readings = readings;
            this.pollutantThresholds = pollutantThresholds;
        }

        public List<SensorReading> getReadings() {
            return readings;
        }

        public HashMap<String, PollutantThreshold> getPollutantThresholds() {
            return pollutantThresholds;
        }
    }
}
