package com.beto4812.airqueue.ui.main.visualizations.viewHolder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.utils.Constants;
import com.squareup.picasso.Picasso;

public class OverviewFragment extends Fragment {

    private View rootView;
    private static final String LOG_TAG = "OverviewFragment";
    private static OverviewFragment instance;
    private SensorReading sensorReading;
    private ImageView imageViewSensor;
    private TextView textViewClosestSensor;
    private TextView textViewLastUpdated;
    private RoundCornerProgressBar progressBarSensibility;
    private RoundCornerProgressBar progressBarQualityIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static OverviewFragment getInstance(){
        if(instance==null){
            instance =  new OverviewFragment();
        }
        return instance;
    }

    public void setSensorReading(SensorReading sensorReading){
        this.sensorReading = sensorReading;
        if(rootView!=null){
            updateUI();
        }
    }

    private void updateUI(){
        Log.v(LOG_TAG, "updateUI");
        if(imageViewSensor!=null){
            Log.v(LOG_TAG, "attempting to render image: " + sensorReading.getImage());
            Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
        }else{
            Log.v(LOG_TAG, "imageView null");
        }
        textViewLastUpdated.setText(sensorReading.getLastUpdated());
        textViewClosestSensor.setText(sensorReading.getSourceID());
        Constants.setLevel(1, progressBarSensibility);
        Constants.setLevel(3, progressBarQualityIndex);
        Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        rootView =  inflater.inflate(R.layout.fragment_overview, container, false);
        imageViewSensor = (ImageView)rootView.findViewById(R.id.image_view_sensor);
        textViewClosestSensor = (TextView)rootView.findViewById(R.id.text_view_closest_sensor);
        textViewLastUpdated = (TextView)rootView.findViewById(R.id.text_view_last_updated);
        progressBarSensibility = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_sensibility_bar);
        progressBarQualityIndex = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_air_quality_bar);
        if(sensorReading!=null){
            updateUI();
        }
        return rootView;
    }

}
