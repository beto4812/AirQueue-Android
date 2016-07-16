package com.beto4812.airqueue.ui.main.pollutantsLinear.viewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder.MultiplePollutantLinearViewHolder;
import com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder.PollutantLinearViewHolder;

import java.util.List;

public class LinearVisualizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int POLLUTANT_SINGLE = 1;
    public static final int POLLUTANT_MULTIPLE = 2;
    private static final String LOG_TAG = "LinearVisualizationAdap";
    private List<Object> items;
    private OnItemClickListener onItemClickListener;

    public LinearVisualizationAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case POLLUTANT_SINGLE:
                return new PollutantLinearViewHolder(layoutInflater.inflate(R.layout.pollutant_linear_view, parent, false));
            case POLLUTANT_MULTIPLE:
                return new MultiplePollutantLinearViewHolder(layoutInflater.inflate(R.layout.pollutant_multiple_linear_view, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof SensorPollutantReadings) {
            return POLLUTANT_SINGLE;
        } else if (items.get(position) instanceof List) {
            return POLLUTANT_MULTIPLE;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case POLLUTANT_SINGLE:
                configurePollutantView((PollutantLinearViewHolder) holder, position);
                break;
            case POLLUTANT_MULTIPLE:
                configureMultiplePollutantView((MultiplePollutantLinearViewHolder)holder, position);
                break;
        }
    }

    private void configurePollutantView(PollutantLinearViewHolder viewHolder, int position) {
        Log.v(LOG_TAG, "configurePollutantView");
        SensorPollutantReadings readings = (SensorPollutantReadings) items.get(position);
        viewHolder.setReadings(readings);
    }

    private void configureMultiplePollutantView(MultiplePollutantLinearViewHolder viewHolder, int position){
        Log.v(LOG_TAG, "configureMultiplePollutantView");
        List<SensorReading> readings = (List) items.get(position);
        viewHolder.setReadings(readings);
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
