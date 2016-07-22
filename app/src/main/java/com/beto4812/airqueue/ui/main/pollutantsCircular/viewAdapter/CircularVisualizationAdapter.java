package com.beto4812.airqueue.ui.main.pollutantsCircular.viewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.ui.main.pollutantsCircular.viewHolder.PollutantInfoViewHolder;
import com.beto4812.airqueue.ui.main.pollutantsCircular.viewHolder.PollutantCircularViewHolder;

import java.util.List;

public class CircularVisualizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String LOG_TAG = "CircularVisualizationAd";


    public static final int POLLUTANT_VIEW = 1;
    public static final int POLLUTANT_INFO = 2;
    private OnItemClickListener onItemClickListener;
    private List<Object> items;
    private int pollutantInfoCount = 0;

    public CircularVisualizationAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POLLUTANT_VIEW:
                View pollutantView = layoutInflater.inflate(R.layout.pollutant_circular_view, parent, false);
                return new PollutantCircularViewHolder(pollutantView);
            case POLLUTANT_INFO:
                //Log.v(LOG_TAG, "onCreateViewHolder POLLUTANT_INFO - pollutantInfoCount: " + pollutantInfoCount);
                View pollutantInfo = layoutInflater.inflate(R.layout.pollutant_info_view, parent, false);
                pollutantInfoCount ++;
                return new PollutantInfoViewHolder(pollutantInfo, pollutantInfoCount);
        }
        Log.v(LOG_TAG, "onCreateViewHolder(): return null");
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Pollutant) {
            return POLLUTANT_VIEW;
        } else if (items.get(position) instanceof PollutantCategoryInfo) {
            return POLLUTANT_INFO;
        }
        return -1;
    }

    public Pollutant getPollutant(int position){
        return (Pollutant) items.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case POLLUTANT_VIEW:
                configurePollutantView((PollutantCircularViewHolder)holder, position);
                break;
            case POLLUTANT_INFO:
                configurePollutantInfo((PollutantInfoViewHolder)holder, position);
                break;
        }
    }

    private void configurePollutantView(PollutantCircularViewHolder viewHolder, int position) {
        Pollutant pollutant = (Pollutant) items.get(position);
        viewHolder.setPollutant(pollutant);
    }


    private void configurePollutantInfo(PollutantInfoViewHolder viewHolder, int position) {
        PollutantCategoryInfo pollutantInfo = (PollutantCategoryInfo) items.get(position);
        viewHolder.setPollutantModel(pollutantInfo);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public void onClick(final View v) {
        onItemClickListener.onItemClick(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}
