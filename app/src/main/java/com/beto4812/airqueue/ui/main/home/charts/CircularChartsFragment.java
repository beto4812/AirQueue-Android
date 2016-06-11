package com.beto4812.airqueue.ui.main.home.charts;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class CircularChartsFragment extends Fragment {

    View mView;

    public CircularChartsFragment() {

    }

    public static CircularChartsFragment newInstance() {
        return new CircularChartsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_circular_charts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        chart1();
        chart2();
        chart3();
        chart4();
        chart5();
        chart6();
    }

    private void chart1() {
        DecoView single = (DecoView) mView.findViewById(R.id.single_circular_chart);

        single.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(10f)
                .build()
        );

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 0)
                .setLineWidth(10f)
                .build();

        int series1Index = single.addSeries(seriesItem1);

        single.addEvent(new DecoEvent.Builder(50).setIndex(series1Index).setDelay(500).build());
    }

    private void chart2() {
        DecoView single = (DecoView) mView.findViewById(R.id.multiple_circular_chart);

        single.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .setLineWidth(20f)
                .build()
        );

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 0)
                .setLineWidth(20f)
                .build();

        int series1Index = single.addSeries(seriesItem1);

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 100, 181, 246))
                .setRange(0, 100, 0)
                .setLineWidth(15f)
                .build();

        int series2Index = single.addSeries(seriesItem2);

        single.addEvent(new DecoEvent.Builder(80).setIndex(series1Index).setDelay(1000).build());
        single.addEvent(new DecoEvent.Builder(40).setIndex(series2Index).setDelay(1250).build());
    }

    private void chart3() {
        DecoView single = (DecoView) mView.findViewById(R.id.no_background_circular_chart);

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 0)
                .setLineWidth(10f)
                .build();

        int series1Index = single.addSeries(seriesItem1);

        single.addEvent(new DecoEvent.Builder(75).setIndex(series1Index).setDelay(1500).build());
    }

    private void chart4() {
        DecoView single = (DecoView) mView.findViewById(R.id.inset_circular_chart);

        single.addSeries(new SeriesItem.Builder(Color.argb(255, 25, 118, 210))
                .setRange(0, 100, 60)
                .setLineWidth(15f)
                .build());

        single.addSeries(new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 40)
                .setLineWidth(15f)
                .setInset(new PointF(15f, 15f))
                .build());

        single.addSeries(new SeriesItem.Builder(Color.argb(255, 100, 181, 246))
                .setRange(0, 100, 20)
                .setLineWidth(15f)
                .setInset(new PointF(30f, 30f))
                .build());
    }

    private void chart5() {
        DecoView single = (DecoView) mView.findViewById(R.id.text_circular_chart);
        final TextView percentText = (TextView) mView.findViewById(R.id.textView9);
        final String format = "%.0f%%";

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 0)
                .setLineWidth(10f)
                .build();

        int series1Index = single.addSeries(seriesItem1);

        single.addEvent(new DecoEvent.Builder(75).setIndex(series1Index).build());

        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                percentText.setText(String.format(format, currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {

            }
        });
    }

    private void chart6() {
        DecoView single = (DecoView) mView.findViewById(R.id.complex_circular_chart);

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 33, 150, 243))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("%.0f%%").build())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                .build();

        int series1Index = single.addSeries(seriesItem1);
        single.addEvent(new DecoEvent.Builder(66).setIndex(series1Index).build());
    }
}