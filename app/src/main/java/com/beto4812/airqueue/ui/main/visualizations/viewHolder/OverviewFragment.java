package com.beto4812.airqueue.ui.main.visualizations.viewHolder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.SensorReading;
import com.squareup.picasso.Picasso;

public class OverviewFragment extends Fragment {

    private View rootView;
    private static final String LOG_TAG = "OverviewFragment";
    private static OverviewFragment instance;
    private SensorReading sensorReading;
    private ImageView imageViewSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static OverviewFragment newInstance() {
        Log.v(LOG_TAG, "newInstance");
        instance = new OverviewFragment();
        return instance;
    }

    public static OverviewFragment getInstance(){
        return instance;
    }

    public void setSensorReading(SensorReading sensorReading){
        this.sensorReading = sensorReading;
        updateUI();
    }

    private void updateUI(){
        Log.v(LOG_TAG, "updateUI");
        if(imageViewSensor!=null){
            Log.v(LOG_TAG, "attempting to render image: " + sensorReading.getImage());
            Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
        }else{
            Log.v(LOG_TAG, "imageView null");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        rootView =  inflater.inflate(R.layout.fragment_overview, container, false);
        this.imageViewSensor = (ImageView)rootView.findViewById(R.id.image_view_sensor);
        if(sensorReading!=null){
            Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
        }
        return rootView;
    }

}
