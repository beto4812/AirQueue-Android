package com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

    public MultiplePollutantLinearViewHolder(View itemView) {
        super(itemView);
        Log.v(LOG_TAG, "MultiplePollutantLinearViewHolder");
        this.rootView = itemView;
        this.lineChart = (LineChart) rootView.findViewById(R.id.pollutant_linear_view_line_chart);
        this.linearLayout = (ViewGroup) rootView.findViewById(R.id.pollutant_multiple_linear_view_linear_layout);
        //checkPollutantView = (CheckPollutantView) rootView.findViewById(R.id.check_pollutant_view1);

        //checkPollutantView.setColor(Color.RED);
        //checkPollutantView.invalidate();
        tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
    }

    public void setReadings(List<SensorPollutantReadings> sensorPollutantReadings) {
        this.sensorPollutantReadings = sensorPollutantReadings;
        setupChart();
    }

    private void setupChart() {
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

            if (Pollutant.RENDERED_POLLUTANTS.contains(sensorReadings.getPollutantCode())) {
                Log.v(LOG_TAG, "adding to line graph: " + sensorReadings.getPollutantCode());
                dataSet = new LineDataSet(sensorReadings.getLineEntries(), sensorReadings.getPollutantCode());
                //dataSet.setDrawCubic(true); //Line curved
                dataSet.setDrawCircles(true);
                dataSet.setCircleRadius(4f);//Circle radius
                dataSets.add(dataSet);
                View pollutantCheckView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.pollutant_check_view, linearLayout, false);
                CheckPollutantView checkPollutantView = (CheckPollutantView) pollutantCheckView.findViewById(R.id.checkPollutantView);
                TextView pollutantName = (TextView) pollutantCheckView.findViewById(R.id.textView);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 5, 0);
                checkPollutantView.setLayoutParams(params);
                pollutantName.setText("Name");
                Log.v(LOG_TAG, "pollutantCode: " + sensorReadings.getPollutantCode());
                checkPollutantView.setColor(Pollutant.allPollutantColors().get(sensorReadings.getPollutantCode()));
                linearLayout.addView(pollutantCheckView);
            }
        }

        lineData = new LineData(xVals, dataSets);
        lineData.setValueTypeface(tf);//Sef font
        lineData.setValueTextSize(9f);//Size font
        lineData.setDrawValues(true);//Draw line values

        lineChart.setData(lineData);
    }
}
