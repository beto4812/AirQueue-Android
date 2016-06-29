package com.beto4812.airqueue.ui.main.visualizations.viewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.ui.main.visualizations.viewHolder.PollutantLinearViewHolder;

import java.util.List;

public class LinearVisualizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String LOG_TAG = "LinearVisualizationAdap";
    private List<Object> items;
    private OnItemClickListener onItemClickListener;

    public LinearVisualizationAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pollutant_linear_view, parent, false);
        v.setOnClickListener(this);
        return new PollutantLinearViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configurePollutantView((PollutantLinearViewHolder) holder, position);
        Log.v(LOG_TAG, "onBindViewHolder");
    }

    private void configurePollutantView(PollutantLinearViewHolder viewHolder, int position) {
        Log.v(LOG_TAG, "configurePollutantView");
        SensorPollutantReadings readings = (SensorPollutantReadings) items.get(position);
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
