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
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.ui.main.pollutantsLinear.viewAdapter.LinearVisualizationAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LinearVisualizationFragment extends Fragment implements VisualizationsFragment.FragmentVisibleInterface {

    private static final String LOG_TAG = "LinearVisualizationFrag";

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private LinearVisualizationAdapter linearVisualizationAdapter;
    private View rootView;
    private static Calendar c = Calendar.getInstance();
    private static LinearVisualizationFragment instance;
    private RelativeLayout headerLayout;
    private List<SensorReading> readings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_pollutants_linear_view, container, false);
        initRecyclerView();
        if(readings!=null){
            updateUI();
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
        List<Object> renderList = new ArrayList<>();
        /*Collection pollutantCodes = Pollutant.allPollutants().keySet();
        List<SensorPollutantReadings> multiplePollutants = new ArrayList<>();
        SensorPollutantReadings sensorPollutantReadings;

        Iterator it = pollutantCodes.iterator();

        //The pollutants over time objects are constructed here. There should be a better way to save them since they are queried from amazon
        for(int i = 0; it.hasNext(); i++){
            String pollutantCode = (String)it.next();
            Log.v(LOG_TAG, "setupUI "+i + " " + pollutantCode);
            sensorPollutantReadings = new SensorPollutantReadings(readings, pollutantCode);
            //renderList.add(sensorPollutantReadings);
            multiplePollutants.add(sensorPollutantReadings);
        }*/
        renderList.add(readings);

        linearVisualizationAdapter = new LinearVisualizationAdapter(renderList);
        recyclerView.setAdapter(linearVisualizationAdapter);
    }

    @Override
    public void fragmentBecameVisible() {

        //headerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        headerLayout.setTranslationY(0);

        new CountDownTimer(3000, 1000) {

            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;


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
