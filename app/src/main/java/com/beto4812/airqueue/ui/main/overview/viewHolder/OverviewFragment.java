package com.beto4812.airqueue.ui.main.overview.viewHolder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.JsonReader;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.home.VisualizationsFragment;
import com.beto4812.airqueue.utils.DataSingelton;
import com.beto4812.airqueue.utils.GetDataNew;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class OverviewFragment extends Fragment implements VisualizationsFragment.FragmentVisibleInterface, GetDataNew.OnDataReceivedListener {

    private static final String LOG_TAG = "OverviewFragment";
    protected View rootView;
    private SensorReading sensorReading;
    private ImageView imageViewSensor;
    private TextView textViewClosestSensor;
    private TextView textViewLastUpdated;
    private RoundCornerProgressBar progressBarSensitivity;
    private RoundCornerProgressBar progressBarQualityIndex;
    private PieChart pieChart;
    private TextView textViewSensitivity;
    private TextView textViewAirQuality;
    private TextView textViewAdviceText;
    private int currentSensitivityLevel = 2;
    private int currentAirQualityIndex = 1;
    private int userAge = 18;
    private boolean firstTime = true;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setSensorReading(SensorReading sensorReading) {
        Log.v(LOG_TAG, "setSensorReading");
        this.sensorReading = sensorReading;
    }

    private void updateUI() {
        if(sensorReading==null){
            this.sensorReading = DataSingelton.getInstance().getSensorReadings().get(DataSingelton.getInstance().getSensorReadings().size()-1);
        }
        Log.v(LOG_TAG, "updateUI getImage: " + sensorReading.getImage());
        Log.v(LOG_TAG, "imageViewSensor: " + imageViewSensor);
        Picasso.with(getContext()).load(sensorReading.getImage()).into(imageViewSensor);
        textViewLastUpdated.setText(sensorReading.getLastUpdated());
        textViewClosestSensor.setText(sensorReading.getSourceID());
        //Constants.setLevel(1, progressBarSensitivity); //Comes from user configuration
        Log.v(LOG_TAG, "airQualityIndex: " + sensorReading.getAirQualityIndexInt());
        setAirQualityToolbar(sensorReading.getAirQualityIndexInt(), textViewAirQuality, progressBarQualityIndex);
        this.currentAirQualityIndex = sensorReading.getAirQualityIndexInt();
        Picasso.with(getContext()).load(sensorReading.getImage()).fit().into(imageViewSensor);
        setupPieChart();
        new GetAdviceData().execute();
    }

    private void setupPieChart() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //for (int c : ColorTemplate.COLORFUL_COLORS)
            //colors.add(c);
        //long seed = System.nanoTime();
        //Collections.shuffle(colors, new Random(seed));

        //Map<Integer, List<Pollutant>> pollutantsByCategory = new TreeMap<>();
        Iterator availablePollutants = sensorReading.getAvailablePollutants().iterator();
        Pollutant pollutant;
        int i = 0;
        int mult = 100;



        while (availablePollutants.hasNext()) {
            pollutant = (Pollutant) availablePollutants.next();
            if(Pollutant.RENDERED_POLLUTANTS.contains(pollutant.getCode())) {
                Log.v(LOG_TAG, "availablePollutant: " + pollutant);
                colors.add(Pollutant.allPollutantColors().get(pollutant.getCode()));
                yVals.add(new Entry(1, i));
                xVals.add(pollutant.getCode());
                i++;
            }
        }

        //Set<Integer> keys = pollutantsByCategory.keySet();

        //int mult = 100;
        //int i = 0;

        //for (int k : keys) {
            //yVals.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
            //xVals.add(Pollutant.PollutantCategory.getPollutantCategoryString(k));
        //    i++;
        //}

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
        this.rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");
        textViewSensitivity = (TextView) rootView.findViewById(R.id.textViewSensitivity);
        textViewAirQuality = (TextView) rootView.findViewById(R.id.textViewAirQuality);
        imageViewSensor = (ImageView) rootView.findViewById(R.id.image_view_sensor);
        textViewClosestSensor = (TextView) rootView.findViewById(R.id.text_view_closest_sensor);
        textViewAdviceText = (TextView) rootView.findViewById(R.id.textView_advice_text);
        textViewClosestSensor.setTypeface(robotoThin);
        textViewLastUpdated = (TextView) rootView.findViewById(R.id.text_view_last_updated);
        textViewLastUpdated.setTypeface(robotoThin);
        progressBarSensitivity = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_sensibility_bar);
        initSensitivityToolbar(progressBarSensitivity);
        progressBarSensitivity.setOnTouchListener(new View.OnTouchListener() {

            private int mActivePointerId = -1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.v(LOG_TAG, "down");
                        final int pointerIndex = MotionEventCompat.getActionIndex(event);
                        final float x = MotionEventCompat.getX(event, pointerIndex);
                        final float y = MotionEventCompat.getY(event, pointerIndex);
                        mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                        setSensitivityToolbar(x, textViewSensitivity, progressBarSensitivity);

                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
                        v.startDrag(null, shadowBuilder, v, 0);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        Log.v(LOG_TAG, "up");
                        mActivePointerId = -1;
                        break;
                }
                Log.v(LOG_TAG, "event.getX(): " + event.getX() + " event.getY(): " + event.getY() + event.getRawX() + " width: " + progressBarSensitivity.getWidth());
                return true;
            }


        });

        progressBarSensitivity.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //Log.v(LOG_TAG, "ACTION_DRAG_STARTED: " + event.getX() + " ," + event.getY());
                    case DragEvent.ACTION_DRAG_ENTERED:
                        //Log.v(LOG_TAG, "ACTION_DRAG_ENTERED: " + event.getX() + " ," + event.getY());
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        //Log.v(LOG_TAG, "ACTION_DRAG_LOCATION: " + event.getX() + " ," + event.getY());
                        setSensitivityToolbar(event.getX(), textViewSensitivity, progressBarSensitivity);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DROP:
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return false;
            }
        });

        progressBarQualityIndex = (RoundCornerProgressBar) rootView.findViewById(R.id.overview_air_quality_bar);
        ((TextView) rootView.findViewById(R.id.textViewAdvice)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewLivePollutants)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textView_advice_text)).setTypeface(openSansBold);
        ((TextView) rootView.findViewById(R.id.textViewPersonalized)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewAirQualityIndex)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewHeader)).setTypeface(openSansBold);
        ((TextView) rootView.findViewById(R.id.textViewClosestSensor)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewLastUpdated)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.textViewSensitivity)).setTypeface(openSansBold);
        ((TextView) rootView.findViewById(R.id.textViewAirQuality)).setTypeface(openSansBold);

        pieChart = (PieChart) rootView.findViewById(R.id.pollutants_chart);
        rootView.findViewById(R.id.imageViewIconInfoPers).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        new SimpleTooltip.Builder(rootView.getContext())
                                .anchorView(v)
                                .text(getString(R.string.personalized_help))
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .build()
                                .show();
                        return false;
                    }
                }
        );

        rootView.findViewById(R.id.imageViewIconInfo).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        new SimpleTooltip.Builder(rootView.getContext())
                                .anchorView(v)
                                .text(getString(R.string.air_quality_sensitivity))
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .build()
                                .show();
                        return false;
                    }
                }
        );

        //imageViewIconInfoLive
        rootView.findViewById(R.id.imageViewIconInfoLive).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        new SimpleTooltip.Builder(rootView.getContext())
                                .anchorView(v)
                                .text(getString(R.string.air_quality_live))
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .build()
                                .show();
                        return false;
                    }
                }
        );

        rootView.findViewById(R.id.imageView_advice).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUntilFinished) {
                        ((TextView) rootView.findViewById(R.id.textView_advice_text)).setTextColor(Color.YELLOW);
                    }
                    public void onFinish() {
                        Log.v(LOG_TAG, "finish");
                        ((TextView) rootView.findViewById(R.id.textView_advice_text)).setTextColor(getResources().getColor(R.color.bold_blue));
                    }
                }.start();
                return false;
            }
        });

        //imageViewIconInfoLive
        rootView.findViewById(R.id.imageViewIconInfoAdvice).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        new SimpleTooltip.Builder(rootView.getContext())
                                .anchorView(v)
                                .text(getString(R.string.air_quality_advice))
                                .gravity(Gravity.TOP)
                                .animated(true)
                                .build()
                                .show();
                        return false;
                    }
                }
        );

        userAge = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("userAge", 18);

        if(DataSingelton.getInstance().isEmpty()){
            Log.v(LOG_TAG, "1");
            GetDataNew.executeGetData(getContext(), this);
        }else{
            Log.v(LOG_TAG, "2");
            onDataReady();
        }

        showInitialTooltip();

        return rootView;
    }

    @Override
    public void fragmentBecameVisible() {
        if(DataSingelton.isSimulated()){
            updateUI();
        }else{
            if (progressBarSensitivity != null) progressBarSensitivity.animate();
            if (pieChart != null) pieChart.animateY(1400, Easing.EasingOption.EaseInCubic);
        }
    }

    private void showInitialTooltip(){
        boolean initialTooltip = PreferenceManager.getDefaultSharedPreferences(rootView.getContext()).getBoolean("initialTooltip", false);

        if(!initialTooltip){
            new SimpleTooltip.Builder(rootView.getContext())
                    .anchorView(rootView.findViewById(R.id.imageViewIconInfoPers))
                    .text(getString(R.string.personalized_help))
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .build()
                    .show();
            PreferenceManager.getDefaultSharedPreferences(rootView.getContext()).edit().putBoolean("initialTooltip", true).commit();
        }
    }

    private void setSensitivityToolbar(float x, TextView textViewSensitivity, RoundCornerProgressBar progressBarSensibility) {
        //Log.v(LOG_TAG, "set: " + x*10/ progressBarSensitivity.getWidth());
        if (x > (progressBarSensibility.getWidth() / 10) * 1) {
            progressBarSensitivity.setProgress(x * 10 / progressBarSensitivity.getWidth());
        }

        if (x > (progressBarSensibility.getWidth() / 3) * 2) {
            setSensitivityToolbarColorLevel(3, progressBarSensibility);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("sensitivityLevel", 3).commit();
        } else if (x > (progressBarSensibility.getWidth() / 3) * 1) {
            setSensitivityToolbarColorLevel(2, progressBarSensibility);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("sensitivityLevel", 2).commit();
        } else {
            setSensitivityToolbarColorLevel(1, progressBarSensibility);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("sensitivityLevel", 1).commit();
        }
        if(currentSensitivityLevel!=PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("sensitivityLevel", 2)){
            new GetAdviceData().execute();
            currentSensitivityLevel = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("sensitivityLevel", 2);
        }
    }

    private void initSensitivityToolbar(RoundCornerProgressBar progressBarSensitivity) {
        int sensitivity = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("sensitivityLevel", 2);
        Log.v(LOG_TAG, "initSensitivityToolbar: sensitivityPreferences: " + sensitivity);
        //3 - 10
        //sensitivity x
        progressBarSensitivity.setProgress(sensitivity * 10 / 3);
        setSensitivityToolbarColorLevel(sensitivity, progressBarSensitivity);
    }

    public void setSensitivityToolbarColorLevel(int level, RoundCornerProgressBar progressBarSensitivity) {
        switch (level) {
            case 1:
                textViewSensitivity.setText("Low sensitivity");
                textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.green_traffic_light));
                progressBarSensitivity.setProgressColor(ContextCompat.getColor(progressBarSensitivity.getContext(), R.color.green_traffic_light));
                break;
            case 2:
                textViewSensitivity.setText("Normal sensitivity");
                textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.yellow_traffic_light));
                progressBarSensitivity.setProgressColor(ContextCompat.getColor(progressBarSensitivity.getContext(), R.color.yellow_traffic_light));
                break;
            case 3:
                textViewSensitivity.setText("High sensitivity");
                textViewSensitivity.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.red_traffic_light));
                progressBarSensitivity.setProgressColor(ContextCompat.getColor(progressBarSensitivity.getContext(), R.color.red_traffic_light));
                break;
        }
    }

    private void setAirQualityToolbar(float x, TextView textViewAirQuality, RoundCornerProgressBar progressBarQualityIndex) {
        Log.v(LOG_TAG, "setAirQualityToolbar: " + x);
        progressBarQualityIndex.setMax(10);
        progressBarQualityIndex.setProgress(x);

        if (x <= 3) {
            textViewAirQuality.setText("Good");
            progressBarQualityIndex.setProgressColor(ContextCompat.getColor(progressBarQualityIndex.getContext(), R.color.green_traffic_light));
            textViewAirQuality.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.green_traffic_light));
        } else if (x <= 6) {
            textViewAirQuality.setText("Regular");
            progressBarQualityIndex.setProgressColor(ContextCompat.getColor(progressBarQualityIndex.getContext(), R.color.yellow_traffic_light));
            textViewAirQuality.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.yellow_traffic_light));
        } else if (x <= 9) {
            textViewAirQuality.setText("Bad");
            progressBarQualityIndex.setProgressColor(ContextCompat.getColor(progressBarQualityIndex.getContext(), R.color.red_traffic_light));
            textViewAirQuality.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.red_traffic_light));
        } else if (x <= 10) {
            textViewAirQuality.setText("Very Bad");
            progressBarQualityIndex.setProgressColor(ContextCompat.getColor(progressBarQualityIndex.getContext(), R.color.black));
            textViewAirQuality.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.black));
        }
    }

    public void onDataReady() {
        Log.v(LOG_TAG, "dataReady");
        if(DataSingelton.getInstance().getSensorReadings().size()>1){
            Log.v(LOG_TAG, DataSingelton.getInstance().getSensorReadings().get(DataSingelton.getInstance().getSensorReadings().size() - 1).toString());
            setSensorReading(DataSingelton.getInstance().getSensorReadings().get(DataSingelton.getInstance().getSensorReadings().size() - 1));
        }
        if (this.rootView != null) {
            updateUI();
        }
    }

    private class GetAdviceData extends AsyncTask<Void, Void, String> {
        //@Override

        @Override
        protected void onPostExecute(String s) {
            if(s!= null){
                textViewAdviceText.setText(s);
            }else{
                textViewAdviceText.setText("Advice is not available");
            }
        }

        @Override
        protected String doInBackground(Void... p) {
            try {
                int sensitivity = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("sensitivityLevel", 2);
                String urlPrefix  = "http://ec2-54-93-97-191.eu-central-1.compute.amazonaws.com:8080/Advice/advice?";
                String urlQueryParams = "age=" + userAge + "&sensitivity=" + sensitivity + "&airQualityIndex=" + currentAirQualityIndex;
                Log.v(LOG_TAG, "httpRequest: " + urlPrefix+urlQueryParams);
                URL url = new URL(urlPrefix+urlQueryParams);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //InputStreamReader isw = new InputStreamReader(in);
                //
                JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                //Log.v(LOG_TAG, " data: ");
                reader.beginObject();

                reader.nextName();
                reader.beginObject();
                reader.nextName();
                reader.beginObject();
                reader.nextName();
                String advice = reader.nextString();
                Log.v(LOG_TAG, "advice: " + advice);
                return advice;
            } catch (Exception e) {
                Log.v(LOG_TAG, e.getLocalizedMessage());

            } finally {
                //urlConnection.disconnect();
            }

            return null;
        }
    }
}
