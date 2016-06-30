package com.beto4812.airqueue.ui.main.visualizations.viewHolder;


import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.Random;

public class PollutantCircularViewHolder extends RecyclerView.ViewHolder {
    public View rootView;
    private DecoView decoView;
    private TextView pollutantNameTextView;
    private TextView pollutantValueTextView;
    private Pollutant pollutant;
    private ImageButton pollutantCenterColor;

    public PollutantCircularViewHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;
    }

    public void setPollutant(Pollutant pollutant){
        this.pollutant = pollutant;
        setupDecoView();
        setupTextViews();
    }

    private void setupTextViews(){
        pollutantNameTextView = (TextView) rootView.findViewById(R.id.pollutant_name);
        pollutantNameTextView.setText(pollutant.getCode());

        pollutantValueTextView = (TextView) rootView.findViewById(R.id.pollutant_value);
        pollutantValueTextView.setText(pollutant.getValue());

        pollutantCenterColor = (ImageButton) rootView.findViewById(R.id.pollutant_center_color);

        switch (pollutant.getColorLevel()){

            case 4:
                pollutantCenterColor.setImageDrawable(rootView.getResources().getDrawable(R.drawable.pollutant_view_black));
                break;
            case 3:
                pollutantCenterColor.setImageDrawable(rootView.getResources().getDrawable(R.drawable.pollutant_view_red));
                break;
            case 2:
                pollutantCenterColor.setImageDrawable(rootView.getResources().getDrawable(R.drawable.pollutant_view_yellow));
                break;
            case 1:
                pollutantCenterColor.setImageDrawable(rootView.getResources().getDrawable(R.drawable.pollutant_view_green));
                break;
        }
    }

    private void setupDecoView(){

        decoView = (DecoView) rootView.findViewById(R.id.single_circular_chart);

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        );

        SeriesItem seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.bright_red))
                .setRange(0, 100, 0)
                .setLineWidth(23f)
                .build();

        int series1Index = decoView.addSeries(seriesItem1);

        SeriesItem seriesItem2 = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.bold_blue))
                .setRange(0, 100, 0)
                .setLineWidth(24f)
                .build();

        int series2Index = decoView.addSeries(seriesItem2);

        SeriesItem seriesItem3 = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.mint_blue))
                .setRange(0, 100, 0)
                .setLineWidth(25f)
                .build();

        int series3Index = decoView.addSeries(seriesItem3);

        Random r = new Random();

        decoView.addEvent(new DecoEvent.Builder(40).setIndex(series1Index).setDelay(700+r.nextInt(800)).build());
        decoView.addEvent(new DecoEvent.Builder(20).setIndex(series2Index).setDelay(500+r.nextInt(800)).build());
        decoView.addEvent(new DecoEvent.Builder(10).setIndex(series3Index).setDelay(300+r.nextInt(800)).build());
    }
}
