package com.beto4812.airqueue.ui.main.pollutantsCircular.viewHolder;

import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.ui.main.pollutantsCircular.PollutantCategoryDetailActivity;
import com.squareup.picasso.Picasso;

public class PollutantInfoViewHolder extends RecyclerView.ViewHolder {

    private static final String LOG_TAG = "PollutantInfoViewHolder";

    public ImageView imageView;
    public TextView text;
    public PollutantCategoryInfo pollutantCategoryInfo;
    private View rootView;
    private FloatingActionButton fab;

    public PollutantInfoViewHolder(View itemView, Integer num) {
        super(itemView);
        this.rootView = itemView;
        Log.v(LOG_TAG, "num: " + num);
    }

    public void setPollutantModel(PollutantCategoryInfo pollutantModel){
        this.pollutantCategoryInfo = pollutantModel;
        setup();
    }


    private void setup(){

        Typeface handOfSean = Typeface.createFromAsset(rootView.getContext().getAssets(), "hos.ttf");
        Typeface openSansRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Regular.ttf");
        Typeface openSansLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Light.ttf");
        Typeface openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        Typeface robotoThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Thin.ttf");

        imageView = (ImageView) itemView.findViewById(R.id.image_view_pollutant);

        Picasso.with(rootView.getContext()).load(pollutantCategoryInfo.getImage()).fit().into(imageView);
        text = (TextView) itemView.findViewById(R.id.textViewPollutantCategory);
        text.setTypeface(openSansLight);
        fab = (FloatingActionButton) itemView.findViewById(R.id.pollutant_info_view_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Snackbar.make(rootView, "More info", Snackbar.LENGTH_SHORT).show();
                PollutantCategoryDetailActivity.navigate(rootView.getContext(), rootView.findViewById(R.id.image), pollutantCategoryInfo);
            }
        });

        text.setText(pollutantCategoryInfo.getPollutantCategoryString());
    }
}