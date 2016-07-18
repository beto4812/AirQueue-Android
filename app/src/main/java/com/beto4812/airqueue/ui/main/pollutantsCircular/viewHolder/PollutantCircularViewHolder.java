package com.beto4812.airqueue.ui.main.pollutantsCircular.viewHolder;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.Random;

public class PollutantCircularViewHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = "PollutantCircularViewHo";

    public View rootView;
    private DecoView decoView;
    private TextView pollutantCodeTextView;
    private TextView pollutantValueTextView;
    private TextView pollutantNameTextView;
    private Pollutant pollutant;
    Typeface openSansLight;
    Typeface openSansBold;
    //private ImageButton pollutantCenterColor;

    public PollutantCircularViewHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;
    }

    public long getItemId(int position) {
        if (pollutant != null) return pollutant.pollutantNumericID();
        else return 0;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
        Log.v(LOG_TAG, "setPollutant: " + pollutant.getCode());
        setupDecoView();
        setupTextViews();
    }

    private void setupTextViews() {
        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");

        pollutantCodeTextView = (TextView) rootView.findViewById(R.id.pollutant_name);
        pollutantCodeTextView.setTypeface(openSansLight);
        pollutantCodeTextView.setText(pollutant.getCode());
        pollutantValueTextView = (TextView) rootView.findViewById(R.id.pollutant_value);
        pollutantCodeTextView.setTypeface(openSansBold);
        pollutantNameTextView = (TextView) rootView.findViewById(R.id.textViewNamePollutant);
        pollutantNameTextView.setText(pollutant.getName());
        pollutantNameTextView.setTypeface(openSansRegular);

        if(pollutant.getValue()!=null){
            pollutantValueTextView.setText(pollutant.getValue()+" "+ pollutant.getMeasureUnit());
        }else{
            pollutantValueTextView.setText("No data");
        }
    }

    private void setupDecoView() {
        decoView = (DecoView) rootView.findViewById(R.id.single_circular_chart);
        int blackOffset = 50; //Extra black space to show

        decoView.addSeries(new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.white))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(30f)
                .build()
        );

        SeriesItem seriesItem1 = null;
        String label = "";

        int color = -1;
        double seriesValue = 0;

        switch (pollutant.getCurrentColorLevel()) {
            case 4:
                color = R.color.myBlack;
                label = "Extremely bad";
                seriesValue = pollutant.getThresholds().getBlackDouble();
                break;
            case 3:
                color = R.color.red_traffic_light;
                label = "Bad";
                seriesValue = pollutant.getThresholds().getRedDouble()+(pollutant.getThresholds().getBlackDouble() - pollutant.getThresholds().getRedDouble())/2;
                break;
            case 2:
                color = R.color.yellow_traffic_light;
                label = "Regular";
                seriesValue = pollutant.getThresholds().getYellowDouble()+(pollutant.getThresholds().getRedDouble() - pollutant.getThresholds().getYellowDouble())/2;
                break;
            case 1:
                color = R.color.green_traffic_light;
                seriesValue = pollutant.getThresholds().getGreenDouble()+(pollutant.getThresholds().getYellowDouble() - pollutant.getThresholds().getGreenDouble())/2;
                label = "Good";
        }

        //Log.v(LOG_TAG, "seriesValue: " + seriesValue);

        seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), color))
                .setRange(0, pollutant.getThresholds().getBlackDouble().floatValue()+blackOffset, 0)
                .setSeriesLabel(new SeriesLabel.Builder(label)
                        .setColorText(Color.argb(255, 255, 255, 255))
                        .setFontSize(10)
                        .setTypeface(openSansLight)
                        .build())
                .setLineWidth(32f)
                .build();

        if(pollutant.getValue()!=null){
            int series1Index = decoView.addSeries(seriesItem1);
            decoView.addEvent(new DecoEvent.Builder(new Float(seriesValue)).setIndex(series1Index).build());
            decoView.invalidate();
        }

        decoView = (DecoView) rootView.findViewById(R.id.single_circular_chart2);
        decoView.addSeries(new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.white))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setLineWidth(30f)
                .build()
        );

        SeriesItem seriesGreen, seriesYellow, seriesRed, seriesBlack;

        seriesGreen = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.green_traffic_light))
                .setRange(0, pollutant.getThresholdMax()+blackOffset, pollutant.getThresholds().getYellowDouble().floatValue())
                //.addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.4f))
                .setInitialVisibility(true)
                .setLineWidth(10f)
                .build();

        //Log.v(LOG_TAG, "seriesGreen: " + pollutant.getThresholds().getYellowDouble().floatValue());

        seriesYellow = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.yellow_traffic_light))
                .setRange(0, pollutant.getThresholdMax()+blackOffset, pollutant.getThresholds().getRedDouble().floatValue())
                //.addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.4f))
                .setInitialVisibility(true)
                .setLineWidth(10f)
                .build();

        //Log.v(LOG_TAG, "seriesYellow: " + pollutant.getThresholds().getRedDouble().floatValue());

        seriesRed = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.red_traffic_light))
                .setRange(0, pollutant.getThresholdMax()+blackOffset, pollutant.getThresholds().getBlackDouble().floatValue())
                //.addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.4f))
                .setInitialVisibility(true)
                .setLineWidth(10f)
                .build();

        //Log.v(LOG_TAG, "seriesRed: " + pollutant.getThresholds().getBlackDouble().floatValue());

        seriesBlack = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.black))
                .setRange(0, pollutant.getThresholdMax()+blackOffset, pollutant.getThresholds().getBlackDouble().floatValue()+blackOffset)
                //.addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.4f))
                .setInitialVisibility(true)
                .setLineWidth(10f)
                .build();

        //Log.v(LOG_TAG, "seriesBlack: " + pollutant.getThresholds().getBlackDouble().floatValue()+blackOffset);

        int seriesIndexGreen, seriesIndexYellow, seriesIndexRed, seriesIndexBlack;
        seriesIndexBlack = decoView.addSeries(seriesBlack);
        //decoView.addEvent(new DecoEvent.Builder(pollutant.getThresholds().getBlackDouble().floatValue()+100).setIndex(seriesIndexBlack).build());

        seriesIndexRed = decoView.addSeries(seriesRed);
        //decoView.addEvent(new DecoEvent.Builder(pollutant.getThresholds().getBlackDouble().floatValue()).setIndex(seriesIndexRed).build());

        seriesIndexYellow = decoView.addSeries(seriesYellow);
        //decoView.addEvent(new DecoEvent.Builder(pollutant.getThresholds().getRedDouble().floatValue()).setIndex(seriesIndexYellow).build());

        seriesIndexGreen = decoView.addSeries(seriesGreen);
        //decoView.addEvent(new DecoEvent.Builder(pollutant.getThresholds().getYellowDouble().floatValue()).setIndex(seriesIndexGreen).build());
    }
}
