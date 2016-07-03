package com.beto4812.airqueue.ui.main.overview.viewHolder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.utils.Constants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class OverviewFragment extends Fragment implements VisualizationsFragment.FragmentVisibleInterface {

    private View rootView;
    private static final String LOG_TAG = "OverviewFragment";
    private static OverviewFragment instance;
    private SensorReading sensorReading;
    private ImageView imageViewSensor;
    private TextView textViewClosestSensor;
    private TextView textViewLastUpdated;
    private RoundCornerProgressBar progressBarSensibility;
    private RoundCornerProgressBar progressBarQualityIndex;
    private PieChart pieChart;


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
        Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
        textViewLastUpdated.setText(sensorReading.getLastUpdated());
        textViewClosestSensor.setText(sensorReading.getSourceID());
        Constants.setLevel(1, progressBarSensibility);
        Constants.setLevel(3, progressBarQualityIndex);
        Picasso.with(getContext()).load(sensorReading.getImage()).fit().into(imageViewSensor);
        setupPieChart();
    }

    private void setupPieChart(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        long seed = System.nanoTime();
        Collections.shuffle(colors, new Random(seed));

        Iterator it = sensorReading.getAvailablePollutants().iterator();
        Map<Integer, List<Pollutant>> pollutantsByCategory= new TreeMap<>();
        Pollutant p;
        while(it.hasNext()){
            p = (Pollutant)it.next();
            if(!pollutantsByCategory.containsKey(p.getCategory())){
                pollutantsByCategory.put(p.getCategory(), new ArrayList<Pollutant>());
            }
            pollutantsByCategory.get(p.getCategory()).add(p);
        }

        Set<Integer> keys = pollutantsByCategory.keySet();

        int mult = 100;
        int i = 0;

        for(int k: keys){
            yVals.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
            xVals.add(Pollutant.PollutantCategory.getPollutantCategoryString(k));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Pollutants");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);

        Typeface tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14f);
        data.setValueTypeface(tf);


        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleColor(ContextCompat.getColor(rootView.getContext(), R.color.colorAccent));
        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        rootView =  inflater.inflate(R.layout.fragment_overview, container, false);
        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");
        final TextView textViewSensitivity = (TextView)rootView.findViewById(R.id.textViewSensitivity);
        final TextView textViewAirQuality = (TextView)rootView.findViewById(R.id.textViewAirQuality);
        imageViewSensor = (ImageView)rootView.findViewById(R.id.image_view_sensor);
        textViewClosestSensor = (TextView)rootView.findViewById(R.id.text_view_closest_sensor);
        textViewClosestSensor.setTypeface(robotoThin);
        textViewLastUpdated = (TextView)rootView.findViewById(R.id.text_view_last_updated);
        textViewLastUpdated.setTypeface(robotoThin);
        progressBarSensibility = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_sensibility_bar);
        progressBarSensibility.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(LOG_TAG, "event.getX(): " + event.getX() + " event.getY(): " + event.getY() + event.getRawX() + " width: " + progressBarSensibility.getWidth());
                if(event.getX()>(progressBarSensibility.getWidth()/3)*2){
                    Constants.setLevel(3, progressBarSensibility);
                    textViewSensitivity.setText("High sensitivity");
                    textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.red_traffic_light));
                }else if(event.getX()>(progressBarSensibility.getWidth()/3)*1){
                    Constants.setLevel(2, progressBarSensibility);
                    textViewSensitivity.setText("Normal sensitivity");
                    textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.yellow_traffic_light));
                }else {
                    Constants.setLevel(1, progressBarSensibility);
                    textViewSensitivity.setText("Low sensitivity");
                    textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.green_traffic_light));
                }
                return true;
            }
        });
        textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.green_traffic_light));
        textViewAirQuality.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.red_traffic_light));

        progressBarQualityIndex = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_air_quality_bar);
        ((TextView) rootView.findViewById(R.id.textViewAdvice)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewLivePollutants)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textView_advice_text)).setTypeface(robotoThin);
        ((TextView) rootView.findViewById(R.id.textViewPersonalized)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewAirQualityIndex)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewHeader)).setTypeface(openSansBold);
        ((TextView) rootView.findViewById(R.id.textViewClosestSensor)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewLastUpdated)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewSensitivity)).setTypeface(openSansBold);
        ((TextView) rootView.findViewById(R.id.textViewAirQuality)).setTypeface(openSansBold);

        pieChart = (PieChart) rootView.findViewById(R.id.pollutants_chart);
        if(sensorReading!=null){
            updateUI();
        }
        return rootView;
    }

    @Override
    public void fragmentBecameVisible() {
        if(progressBarSensibility!=null) progressBarSensibility.animate();
        if(pieChart!=null) pieChart.animateY(1400, Easing.EasingOption.EaseInCubic);
    }
}
