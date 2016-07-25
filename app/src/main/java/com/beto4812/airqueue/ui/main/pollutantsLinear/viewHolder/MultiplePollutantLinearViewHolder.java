package com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorPollutantReadings;
import com.beto4812.airqueue.model.SensorReading;
import com.beto4812.airqueue.ui.customView.CheckPollutantView;
import com.beto4812.airqueue.ui.customView.OverlayLinearVIew;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MultiplePollutantLinearViewHolder extends RecyclerView.ViewHolder{

    private static final String LOG_TAG = "PollutantLinearViewHold";

    public View rootView;
    private List<SensorPollutantReadings> sensorPollutantReadings;
    private List<SensorReading> sensorReadings;
    private LineChart lineChart;
    private ArrayList<Entry> yVals;
    private ArrayList<String> xVals;
    private LineData lineData;
    private Typeface tf;
    private ViewGroup linearLayout;
    private Set<String> graphicablePollutants;
    private OnPollutantSelectedListener pollutantSelectedListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    //private TimePickerDialog.OnTimeSetListener timeSetListener;
    private GregorianCalendar fromCalendar, toCalendar;
    private String closestSensorID = "";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private HashMap<String, View> checkPollutantViews;
    private boolean pollutantPanelSetup = false;
    private TextView headerTextView;
    private OverlayLinearVIew overlayLinearVIew;
    private MultiplePollutantLinearViewHolder instance;
    private HashMap<String, PollutantThreshold> pollutantThresholds;
    private int lastEntryHour = 0;
    private Typeface openSansBold;


    public MultiplePollutantLinearViewHolder(View itemView) {
        super(itemView);
        Log.v(LOG_TAG, "MultiplePollutantLinearViewHolder");
        init();
    }

    public MultiplePollutantLinearViewHolder getInstance(){
        if(instance==null){
            this.instance = new MultiplePollutantLinearViewHolder(itemView);
        }

        return instance;
    }



    public void init() {
        this.rootView = itemView;
        this.lineChart = (LineChart) rootView.findViewById(R.id.pollutant_linear_view_line_chart);
        this.linearLayout = (ViewGroup) rootView.findViewById(R.id.pollutant_multiple_linear_view_linear_layout);
        this.headerTextView = (TextView) rootView.findViewById(R.id.pollutant_multiple_linear_view_date_text);
        this.overlayLinearVIew = (OverlayLinearVIew) rootView.findViewById(R.id.pollutant_linear_view_overlay);
        tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "Roboto-Regular.ttf");
        openSansBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "OpenSans-Bold.ttf");
        TextView textView = (TextView)(rootView.findViewById(R.id.textView3));
        textView.setTypeface(openSansBold);
        closestSensorID = PreferenceManager.getDefaultSharedPreferences(rootView.getContext()).getString("closestSourceID", null);
        Log.v(LOG_TAG, "closestSensorID: " + closestSensorID);

        pollutantSelectedListener = new OnPollutantSelectedListener() {
            @Override
            public void onPollutantSelected(String code) {
                Log.v(LOG_TAG, "touch from parent: " + code);
                if(code!=graphicablePollutants.iterator().next()){
                    CheckPollutantView out = (CheckPollutantView) checkPollutantViews.get(graphicablePollutants.iterator().next());
                    out.setSelected(false);
                    CheckPollutantView in = (CheckPollutantView) checkPollutantViews.get(code);
                    in.setSelected(true);
                    graphicablePollutants.clear();
                    graphicablePollutants.add(code);
                    setupChart();
                    refreshOverlayPanel();
                }
            }

            @Override
            public void onPollutantDeSelected(String code) {
            }

            public void printGraphicablePollutants() {
                Iterator it = graphicablePollutants.iterator();
                while (it.hasNext()) {
                    Log.v(LOG_TAG, "it: " + it.next());
                }
            }
        };
        setupPickers();
    }

    public void refreshOverlayPanel(){
        Log.v(LOG_TAG, "refreshOverlayPanel");
        Iterator it = checkPollutantViews.values().iterator();
        int selected = 0;
        String selectedCode = "";
        while (it.hasNext()){
            CheckPollutantView view = (CheckPollutantView)it.next();
            if(view.isSelected()){
                selected ++;
                selectedCode = view.getPollutantCode();
            }
        }

        Log.v(LOG_TAG, "lineChartY: " + lineChart.getData().getYMax());

        if(selected == 1){
            overlayLinearVIew.setPollutantThreshold(pollutantThresholds.get(selectedCode));
            overlayLinearVIew.setMaxYValue((int)lineChart.getData().getYMax());
            overlayLinearVIew.setEnabled(true);
        }else{
            overlayLinearVIew.setEnabled(false);
        }
    }

    public void setupPickers() {
        //Set from and to to today
        fromCalendar = (GregorianCalendar) Calendar.getInstance();
        toCalendar = fromCalendar;

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                fromCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0, 0);
                toCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, 23, 59, 59);
                Log.v(LOG_TAG, " from: " + dateFormatter.format(fromCalendar.getTime()) + " to: " + dateFormatter.format(toCalendar.getTime()));
                Log.v(LOG_TAG, "fromCalendar: " + dateFormatter.format(fromCalendar.getTime()) + " toCalendar: " + dateFormatter.format(toCalendar.getTime()));
                new GetData().execute();
            }
        };

        (rootView.findViewById(R.id.pollutant_multiple_linear_view_date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        dateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(((Activity) rootView.getContext()).getFragmentManager(), "Datepickerdialog");
            }
        });

    }

    public void setPollutantThresholds(HashMap<String, PollutantThreshold> thresholds){
        this.pollutantThresholds = thresholds;
    }

    public void setReadings(List<SensorReading> sensorReadings) {
        this.sensorReadings = sensorReadings;
        this.sensorPollutantReadings = new ArrayList<>();
        Iterator it = Pollutant.RENDERED_POLLUTANTS.iterator();
        if(graphicablePollutants!=null && !graphicablePollutants.isEmpty()){
            CheckPollutantView out = (CheckPollutantView) checkPollutantViews.get(graphicablePollutants.iterator().next());
            out.setSelected(false);
        }
        graphicablePollutants = new TreeSet<>();
        graphicablePollutants.add(Pollutant.RENDERED_POLLUTANTS.iterator().next());
        SensorPollutantReadings reading;

        //The pollutants over time objects are constructed here. There should be a better way to save them since they are queried from amazon
        for (int i = 0; it.hasNext(); i++) {
            String pollutantCode = (String) it.next();
            //Log.v(LOG_TAG, "setupUI " + i + " " + pollutantCode);
            reading = new SensorPollutantReadings(sensorReadings, pollutantCode);
            this.sensorPollutantReadings.add(reading);
        }
        if(!pollutantPanelSetup){
            setupPollutantsPanel();
            pollutantPanelSetup = true;
        }else{
            refreshPollutantsPanel();
        }
        setupChart();
        refreshOverlayPanel();
        setupHeaderText();
    }

    private void setupHeaderText(){
        String date = "";
        String hour = "";

        if(this.fromCalendar.get(Calendar.DAY_OF_MONTH)==this.toCalendar.get(Calendar.DAY_OF_MONTH)){
            date = "Date: Today";
        }else{
            date = "Date: "+fromCalendar.get(Calendar.DAY_OF_MONTH)+"/"+fromCalendar.get(Calendar.MONTH)+"-"+toCalendar.get(Calendar.DAY_OF_MONTH)+"/"+toCalendar.get(Calendar.MONTH);
        }

        headerTextView.setText(String.format("%1$tA %1$tb %1$td", fromCalendar));

    }

    private void setupChart() {
        setupData();
        //lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.animateX(500, Easing.EasingOption.EaseInCubic);
        lineChart.getLegend().setEnabled(false);
        //lineChart.setDescription(Pollutant.allPollutants().get(sensorPollutantReadings.getPollutantCode())); //Chart legend text
        lineChart.getAxisRight().setEnabled(true); //Right y axis legends
        lineChart.getAxisRight().setDrawGridLines(true);//Both right and left x axis should be true or false
        lineChart.getAxisRight().setDrawLabels(false); //Label on the right line Y axis

        lineChart.getAxisLeft().setEnabled(true); //Left Y axis legends
        lineChart.getAxisLeft().setDrawGridLines(true);//Both right and left x axis should be true or false
        lineChart.getAxisLeft().setDrawLabels(true);//Label on the left line Y axis

        lineChart.getXAxis().setEnabled(true); //Top x axis legends
        //Log.v(LOG_TAG, "xVals size: " + xVals.size());
        if (xVals.size() < 10) {
            lineChart.getXAxis().setLabelsToSkip(0);
        } else {
            lineChart.getXAxis().resetLabelsToSkip();
        }

        //lineChart.getXAxis().setLabelsToSkip(0); //Sets the number of labels that should be skipped on the axis before the next label is drawn.
        lineChart.getXAxis().setDrawGridLines(true);//Both right and left x axis should be true or false
        lineChart.getXAxis().setDrawLabels(true); //X axis bottom labels (x hours)
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //lineChart.getXAxis().getGridDashPathEffect()
        lineChart.getXAxis().setDrawAxisLine(false); //Draw top x line in background

        lineChart.setDrawGridBackground(false);//Fill color of grids
        //lineChart.setGridBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.light_orange));
        lineChart.setDescription("hours");//Small text right bottom corner
        lineChart.invalidate();//Refresh
    }

    private void setupPollutantsPanel() {
        checkPollutantViews = new HashMap<>();
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        SensorPollutantReadings sensorReadings;
        Space space;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, 1);

        params2.weight = 1;
        space = new Space(rootView.getContext());
        space.setLayoutParams(params2);
        linearLayout.removeAllViews();
        linearLayout.addView(space);
        boolean firstView = true;
        while (sensorPollutantReadingsIterator.hasNext()) {
            sensorReadings = (SensorPollutantReadings) sensorPollutantReadingsIterator.next();
            if(Pollutant.RENDERED_POLLUTANTS.contains(sensorReadings.getPollutantCode())){
                //Log.v(LOG_TAG, "adding to panel: " + sensorReadings.getPollutantCode());
                View pollutantCheckView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.pollutant_check_view, linearLayout, false);
                CheckPollutantView checkPollutantView = (CheckPollutantView) pollutantCheckView.findViewById(R.id.checkPollutantView);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                //params.setMargins(40, 0, 40, 0);
                checkPollutantView.setLayoutParams(params);
                checkPollutantView.setTextView((TextView) pollutantCheckView.findViewById(R.id.textViewNoData));
                //pollutantName.setText(sensorReadings.getPollutantCode());//sensorReadings.getPollutantCode()
                //Log.v(LOG_TAG, "pollutantCode: " + sensorReadings.getPollutantCode());
                checkPollutantView.setColor(Pollutant.allPollutantColors().get(sensorReadings.getPollutantCode()));
                checkPollutantView.setPollutantCode(sensorReadings.getPollutantCode());
                if (!sensorReadings.hasData()) {
                    checkPollutantView.setDisabled();
                } else {
                    if(firstView){
                        checkPollutantView.setSelected(true);
                        firstView = false;
                    }
                }
                checkPollutantView.setListener(pollutantSelectedListener);
                space = new Space(rootView.getContext());
                space.setLayoutParams(params2);
                linearLayout.addView(pollutantCheckView, params);
                checkPollutantViews.put(sensorReadings.getPollutantCode(), checkPollutantView);
                linearLayout.addView(space);
            }
        }
        linearLayout.invalidate();
    }

    public void refreshPollutantsPanel(){
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        SensorPollutantReadings sensorReadings;
        boolean firstView = true;

        while (sensorPollutantReadingsIterator.hasNext()) {
            sensorReadings = (SensorPollutantReadings) sensorPollutantReadingsIterator.next();
            if(Pollutant.RENDERED_POLLUTANTS.contains(sensorReadings.getPollutantCode())) {
                CheckPollutantView checkPollutantView = (CheckPollutantView)checkPollutantViews.get(sensorReadings.getPollutantCode());
                if (!sensorReadings.hasData()) {
                    checkPollutantView.setDisabled();
                    checkPollutantView.setSelected(false);
                } else {
                    checkPollutantView.setEnabled();
                    if(firstView){
                        checkPollutantView.setSelected(true);
                        firstView = false;
                    }
                }
            }
        }
    }


    private void setupData() {
        Iterator sensorPollutantReadingsIterator = sensorPollutantReadings.iterator();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        SensorPollutantReadings sensorPollutant;
        LineDataSet dataSet;

        int lastEntryHour = 0;

        boolean first = true;
        while (sensorPollutantReadingsIterator.hasNext()) {
            sensorPollutant = (SensorPollutantReadings) sensorPollutantReadingsIterator.next();
            if (graphicablePollutants.contains(sensorPollutant.getPollutantCode())) {

                Log.v(LOG_TAG, "adding to line graph: " + sensorPollutant.getPollutantCode());
                lastEntryHour = sensorPollutant.getLastEntryHour() > lastEntryHour ? sensorPollutant.getLastEntryHour() : lastEntryHour;
                sensorPollutant.getLineEntries().size();
                dataSet = new LineDataSet(sensorPollutant.getLineEntries(), sensorPollutant.getPollutantCode());
                //Log.v(LOG_TAG, "dataset size: " + sensorPollutant.getLineEntries().size());
                dataSet.setDrawCubic(true); //Line curved
                dataSet.setDrawCircles(true);
                dataSet.setCircleRadius(4f);//Circle radius
                dataSet.setCircleColor(Pollutant.allPollutantColors().get(sensorPollutant.getPollutantCode()));
                dataSet.setColor(Color.BLACK);
                dataSets.add(dataSet);
            }
        }


        Iterator it = sensorReadings.iterator();
        Log.v(LOG_TAG, "LastEntryHour: " + lastEntryHour);
        Log.v(LOG_TAG, "sensorReadings size: " + sensorReadings.size());
        SensorReading sensorReading;

        xVals = new ArrayList<>();

        //xVals.add("00:00");
        int currentHour = 0;
        for(int i = 0; i<=lastEntryHour; i++){
            //sensorReading = (SensorReading) sensorReadings.get(i);
            //Log.v(LOG_TAG, "add to xVals: " + "" + i + ":00");
            xVals.add("" + i + ":00");
        }


        lineData = new LineData(xVals, dataSets);
        lineData.setValueTypeface(tf);//Sef font
        lineData.setValueTextSize(9f);//Size font
        lineData.setDrawValues(false);
        if (xVals.size() > 24) {
            lineData.setDrawValues(false);//Draw line values
        }
        lineChart.setData(lineData);
    }


    public interface OnPollutantSelectedListener {
        void onPollutantSelected(String code);
        void onPollutantDeSelected(String code);
    }


    private class GetData extends AsyncTask<Void, Void, List<SensorReading>> {

        protected void onPostExecute(List<SensorReading> s) {
            if (s != null) {
                setReadings(s);
            }
        }

        @Override
        protected List<SensorReading> doInBackground(Void... params) {
            List<SensorReading> sensorReadings = AWSClientManager.defaultMobileClient().getDynamoDbManager().
                    getReadingsBySourceID(closestSensorID, dateFormatter.format(fromCalendar.getTime()), dateFormatter.format(toCalendar.getTime()));
            Iterator it = sensorReadings.iterator();
            while (it.hasNext()) {
                Log.v(LOG_TAG, "data: " + it.next().toString());
            }
            return sensorReadings;
        }
    }

}
