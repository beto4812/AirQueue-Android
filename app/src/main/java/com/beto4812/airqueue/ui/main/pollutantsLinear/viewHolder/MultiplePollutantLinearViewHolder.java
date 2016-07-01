package com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.beto4812.airqueue.R;
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

    public MultiplePollutantLinearViewHolder(View itemView) {
        super(itemView);
        Log.v(LOG_TAG, "MultiplePollutantLinearViewHolder");
        this.rootView = itemView;
        lineChart = (LineChart) rootView.findViewById(R.id.pollutant_linear_view_line_chart);
        checkPollutantView = (CheckPollutantView) rootView.findViewById(R.id.check_pollutant_view);
        checkPollutantView.setColor(Color.RED);
        checkPollutantView.invalidate();
        tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
    }

    public void setReadings(List<SensorPollutantReadings> sensorPollutantReadings){
        this.sensorPollutantReadings = sensorPollutantReadings;
        setupChart();
    }

    private void setupChart(){
        setupData();
        //lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.animateX(1000, Easing.EasingOption.EaseInBounce);
        lineChart.getLegend().setEnabled(true);
        //lineChart.setDescription(Pollutant.allPollutants().get(sensorPollutantReadings.getPollutantCode())); //Chart legend text
        lineChart.getAxisLeft().setEnabled(false); //Left Y axis legends
        lineChart.getAxisRight().setEnabled(false); //Right y axis legends
        lineChart.getXAxis().setEnabled(true); //Top x axis legends
        lineChart.getXAxis().setDrawAxisLine(false); //Draw x line in background
        //lineChart.setDrawGridBackground(false);//Fill color of grids
        //lineChart.setGridBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));

        lineChart.invalidate();//Refresh
    }

    private void setupData(){
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        SensorPollutantReadings sensorReadings;
        LineDataSet dataSet;


        xVals = new ArrayList<>();

        for(int i = 0; i<24; i++){
            xVals.add((i) + "");
        }

        while(sensorPollutantReadingsIterator.hasNext()){
            sensorReadings = (SensorPollutantReadings)sensorPollutantReadingsIterator.next();
            dataSet = new LineDataSet(sensorReadings.getLineEntries(), sensorReadings.getPollutantCode());
            //dataSet.setDrawCubic(true); //Line curved
            dataSet.setDrawCircles(true);
            dataSet.setCircleRadius(4f);//Circle radius

            dataSets.add(dataSet);
        }

        lineData = new LineData(xVals, dataSets);
        lineData.setValueTypeface(tf);//Sef font
        lineData.setValueTextSize(9f);//Size font
        lineData.setDrawValues(true);//Draw line values

        lineChart.setData(lineData);
    }
}
