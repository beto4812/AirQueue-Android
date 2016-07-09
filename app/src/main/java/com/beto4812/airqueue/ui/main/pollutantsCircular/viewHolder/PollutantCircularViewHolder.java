package com.beto4812.airqueue.ui.main.pollutantsCircular.viewHolder;


import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.utils.CircleTransform;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class PollutantCircularViewHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = "PollutantCircularViewHo";

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

    public long getItemId(int position) {
        if (pollutant != null) return pollutant.pollutantNumericID();
        else return 0;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
        Log.v(LOG_TAG, "setPollutant");
        setupDecoView();
        setupTextViews();
    }

    private void setupTextViews() {
        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");

        pollutantNameTextView = (TextView) rootView.findViewById(R.id.pollutant_name);
        pollutantNameTextView.setTypeface(openSansLight);
        pollutantNameTextView.setText(pollutant.getCode());
        pollutantValueTextView = (TextView) rootView.findViewById(R.id.pollutant_value);
        pollutantNameTextView.setTypeface(openSansLight);

        ((TextView) rootView.findViewById(R.id.pollutant_name)).setTypeface(openSansLight);
        ((TextView) rootView.findViewById(R.id.pollutant_value)).setTypeface(openSansBold);
        pollutantValueTextView.setText(pollutant.getValue());

        pollutantCenterColor = (ImageButton) rootView.findViewById(R.id.pollutant_center_color);

        switch (pollutant.getColorLevel()) {
            case 4:
                Picasso.with(rootView.getContext()).load(R.drawable.pollutant_view_black).transform(new CircleTransform()).fit().into(pollutantCenterColor);
                break;
            case 3:
                Picasso.with(rootView.getContext()).load(R.drawable.pollutant_view_red).transform(new CircleTransform()).fit().into(pollutantCenterColor);
                break;
            case 2:
                Picasso.with(rootView.getContext()).load(R.drawable.pollutant_view_yellow).transform(new CircleTransform()).fit().into(pollutantCenterColor);
                break;
            case 1:
                Picasso.with(rootView.getContext()).load(R.drawable.pollutant_view_green).transform(new CircleTransform()).fit().into(pollutantCenterColor);
                break;
            default:
                Picasso.with(rootView.getContext()).load(R.drawable.pollutant_view_black).transform(new CircleTransform()).fit().into(pollutantCenterColor);
        }
    }

    private void setupDecoView() {
        decoView = (DecoView) rootView.findViewById(R.id.single_circular_chart);

        decoView.addSeries(new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), R.color.white))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(30f)
                .build()  
        );

        SeriesItem seriesItem1 = null;

        int color = -1;

        switch (pollutant.getColorLevel()) {
            case 4:
                color = R.color.myBlack;
                break;
            case 3:
                color = R.color.red_traffic_light;
                break;
            case 2:
                color = R.color.yellow_traffic_light;
                break;
            case 1:
                color = R.color.green_traffic_light;
        }

        seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(rootView.getContext(), color))
                .setRange(0, pollutant.getThresholdMax(), 0)
                .setLineWidth(32f)
                .build();

        int series1Index = decoView.addSeries(seriesItem1);
        Random r = new Random();

        decoView.addEvent(new DecoEvent.Builder((float)pollutant.getDoubleValue()).setIndex(series1Index).build());
        decoView.invalidate();
    }
}
