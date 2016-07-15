package com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.ui.customView.CheckPollutantView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiplePollutantLinearViewHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = "PollutantLinearViewHold";

    public View rootView;
    private List<SensorPollutantReadings> sensorPollutantReadings;
    private LineChart lineChart;
    private ArrayList<Entry> yVals;
    private ArrayList<String> xVals;
    private LineData lineData;
    private Typeface tf;
    private CheckPollutantView checkPollutantView;
    private ViewGroup linearLayout;
    private Set<String> graphicablePollutants;
    OnPollutantSelectedListener listener;

    public MultiplePollutantLinearViewHolder(View itemView) {
        super(itemView);
        Log.v(LOG_TAG, "MultiplePollutantLinearViewHolder");
        init();
    }


    public void init(){
        this.rootView = itemView;
        this.lineChart = (LineChart) rootView.findViewById(R.id.pollutant_linear_view_line_chart);
        this.linearLayout = (ViewGroup) rootView.findViewById(R.id.pollutant_multiple_linear_view_linear_layout);
        graphicablePollutants = Pollutant.RENDERED_POLLUTANTS;
        tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");

        listener  = new OnPollutantSelectedListener() {
            @Override
            public void onPollutantSelected(String code) {
                Log.v(LOG_TAG, "touch from parent: " + code);
                graphicablePollutants.add(code);
                setupChart();
            }

            @Override
            public void onPollutantDeSelected(String code) {
                Log.v(LOG_TAG, "touch from parent: " + code);
                graphicablePollutants.remove(code);
                setupChart();
            }

            public void printGraphicablePollutants(){
                Iterator it = graphicablePollutants.iterator();
                while (it.hasNext()){
                    Log.v(LOG_TAG, "it: " + it.next());
                }
            }
        };

    }

    public void setReadings(List<SensorPollutantReadings> sensorPollutantReadings) {
        this.sensorPollutantReadings = sensorPollutantReadings;
        setupPollutantsPanel();
        setupChart();
    }

    private void setupChart() {
        setupData();
        //lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.animateX(500, Easing.EasingOption.EaseInCubic);
        lineChart.getLegend().setEnabled(false);
        //lineChart.setDescription(Pollutant.allPollutants().get(sensorPollutantReadings.getPollutantCode())); //Chart legend text
        lineChart.getAxisLeft().setEnabled(true); //Left Y axis legends
        lineChart.getAxisRight().setEnabled(true); //Right y axis legends
        lineChart.getXAxis().setEnabled(false); //Top x axis legends
        lineChart.getXAxis().setDrawAxisLine(false); //Draw x line in background
        lineChart.setDrawGridBackground(false);//Fill color of grids
        //lineChart.setGridBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));
        lineChart.setDescription("Esto");
        lineChart.invalidate();//Refresh
    }

    private void setupPollutantsPanel(){
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        SensorPollutantReadings sensorReadings;
        Space space;
        LinearLayout.LayoutParams params2 =  new LinearLayout.LayoutParams(0,1);

        params2.weight = 1;
        space = new Space(rootView.getContext());
        space.setLayoutParams(params2);
        linearLayout.addView(space);
        while (sensorPollutantReadingsIterator.hasNext()) {
            sensorReadings = (SensorPollutantReadings) sensorPollutantReadingsIterator.next();
            if (graphicablePollutants.contains(sensorReadings.getPollutantCode())) {

                View pollutantCheckView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.pollutant_check_view, linearLayout, false);
                CheckPollutantView checkPollutantView = (CheckPollutantView) pollutantCheckView.findViewById(R.id.checkPollutantView);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                //params.setMargins(40, 0, 40, 0);
                checkPollutantView.setLayoutParams(params);
                checkPollutantView.setTextView((TextView)pollutantCheckView.findViewById(R.id.textViewNoData));
                //pollutantName.setText(sensorReadings.getPollutantCode());//sensorReadings.getPollutantCode()
                Log.v(LOG_TAG, "pollutantCode: " + sensorReadings.getPollutantCode());
                checkPollutantView.setColor(Pollutant.allPollutantColors().get(sensorReadings.getPollutantCode()));
                checkPollutantView.setPollutantCode(sensorReadings.getPollutantCode());
                if(!sensorReadings.hasData()){
                    checkPollutantView.setDisabled();
                }else{
                    checkPollutantView.setSelected();
                }
                checkPollutantView.setListener(listener);
                space = new Space(rootView.getContext());
                space.setLayoutParams(params2);
                linearLayout.addView(pollutantCheckView, params);
                linearLayout.addView(space);
            }
        }
        linearLayout.invalidate();
    }

    private void setupData() {
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        SensorPollutantReadings sensorReadings;
        LineDataSet dataSet;

        xVals = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            xVals.add((i) + "");
        }

        while (sensorPollutantReadingsIterator.hasNext()) {
            sensorReadings = (SensorPollutantReadings) sensorPollutantReadingsIterator.next();
            if (graphicablePollutants.contains(sensorReadings.getPollutantCode())) {
                Log.v(LOG_TAG, "adding to line graph: " + sensorReadings.getPollutantCode());
                dataSet = new LineDataSet(sensorReadings.getLineEntries(), sensorReadings.getPollutantCode());
                //dataSet.setDrawCubic(true); //Line curved
                dataSet.setDrawCircles(true);
                dataSet.setCircleRadius(4f);//Circle radius
                dataSet.setCircleColor(Pollutant.allPollutantColors().get(sensorReadings.getPollutantCode()));
                dataSet.setColor(Color.BLACK);
                dataSets.add(dataSet);
            }
        }

        lineData = new LineData(xVals, dataSets);
        lineData.setValueTypeface(tf);//Sef font
        lineData.setValueTextSize(9f);//Size font
        lineData.setDrawValues(true);//Draw line values

        lineChart.setData(lineData);
    }

    public interface OnPollutantSelectedListener {
        void onPollutantSelected(String code);
        void onPollutantDeSelected(String code);
    }
}
