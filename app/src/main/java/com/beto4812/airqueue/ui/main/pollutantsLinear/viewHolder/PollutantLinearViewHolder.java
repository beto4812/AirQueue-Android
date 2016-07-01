package com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PollutantLinearViewHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = "PollutantLinearViewHold";

    public View rootView;
    private SensorPollutantReadings sensorPollutantReadings;
    private LineChart lineChart;
    private ArrayList<Entry> yVals;
    private ArrayList<String> xVals;
    private LineData lineData;
    private Typeface tf;

    public PollutantLinearViewHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;
        lineChart = (LineChart) rootView.findViewById(R.id.pollutant_linear_view_line_chart);
        tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
    }

    public void setReadings(SensorPollutantReadings sensorPollutantReadings){
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
        lineChart.setDescription(Pollutant.allPollutants().get(sensorPollutantReadings.getPollutantCode())); //Chart legend text
        lineChart.getAxisLeft().setEnabled(false); //Left Y axis legends
        lineChart.getAxisRight().setEnabled(false); //Right y axis legends
        lineChart.getXAxis().setEnabled(true); //Top x axis legends
        lineChart.getXAxis().setDrawAxisLine(false); //Draw x line in background
        //lineChart.setDrawGridBackground(false);//Fill color of grids
        //lineChart.setGridBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));

        lineChart.invalidate();//Refresh
    }

    private void setupData(){
        Log.v(LOG_TAG, "setupData: " + sensorPollutantReadings.getPollutantCode());
        HashMap<String, Pollutant> timePollutants = sensorPollutantReadings.getTimePollutants();
        SortedSet<String> keys = new TreeSet<String>(timePollutants.keySet());
        Iterator it = keys.iterator();

        xVals = new ArrayList<>();

        for(int i = 0; i<24; i++){


        }
        if(lineChart.getData()==null){
            xVals = new ArrayList<>();
            yVals = new ArrayList<>();

            int i = 0;
            String key;
            Pollutant temp;
            int hour;
            while (it.hasNext()){
                key = (String)it.next();
                temp = timePollutants.get(key);
                hour = Integer.parseInt(key.split(" ")[1].split(":")[0]);
                Log.v(LOG_TAG, "setupData "+i+ " value: " + temp.getDoubleValue() + " hour: " + hour);
                xVals.add(hour+"hrs");
                yVals.add(new Entry((float)temp.getDoubleValue(), hour-1));
                i++;
            }

            LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setDrawCubic(true); //Line curved
            //set1.setDrawStepped(true); //Line in steps
            set1.setCubicIntensity(0.2f);

            set1.setDrawFilled(true);


            set1.setDrawCircles(true);//Circles on each value
            set1.setCircleRadius(4f);//Circle radius
            set1.setCircleColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));//Circle color


            set1.setLineWidth(2.5f);//Line width
            set1.setHighLightColor(ContextCompat.getColor(rootView.getContext(), R.color.soft_white));//Highlight line when selected
            set1.setColor(ContextCompat.getColor(rootView.getContext(), R.color.white));
            set1.setFillColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));

            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new FillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            lineData = new LineData(xVals, dataSets);
            lineData.setValueTypeface(tf);//Sef font
            lineData.setValueTextSize(9f);//Size font
            lineData.setDrawValues(true);//Draw line values

            lineChart.setData(lineData);
        }
    }
}
