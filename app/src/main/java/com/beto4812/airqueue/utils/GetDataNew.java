package com.beto4812.airqueue.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.beto4812.airqueue.aws.AWSClientManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDataNew extends AsyncTask<Void, Void, Void> {

    //private List<OnDataReceivedListener> listeners = new ArrayList<>();
    private OnDataReceivedListener listener;
    private Object data;
    private String latitude, longitude;
    private Context appContext;
    private static final String LOG_TAG = "OverviewFragment";
    private static Boolean simulated = false;

    public void setAppContext(Context context){
        this.appContext = context;
    }

    public void setListener(OnDataReceivedListener listener) {
        this.listener = listener;
    }


    public void setLastKnownCoordinates(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static void setSimulated(boolean set){
        simulated = set;
    }

    /**
     * Role of doInBackground filling data singleton from data simulator
     */
    private void fillSimulatedData(){
        DataSimulator sim = new DataSimulator();
        DataSingelton.getInstance().setPollutantThresholds(sim.getPollutantThresholds());
        DataSingelton.getInstance().setPollutantCategoriesInfo(sim.getPollutantCategoriesInfo());
        DataSingelton.getInstance().setSensorReadings(sim.getSensorReadings());

    }

    /**
     * Notifies listeners
     */
    private void simulateExecution(){
        Log.v(LOG_TAG, "simulateExecution");
        listener.onDataReady();
    }

    public static boolean isSimulated(){
        return simulated;
    }

    public static void executeGetData(Context context, OnDataReceivedListener listener){
        Log.v(LOG_TAG, "----------------------------executeGetData-------------");

        GetDataNew getDataNew = new GetDataNew();
        if(!simulated){

            getDataNew.setAppContext(context);
            getDataNew.setLastKnownCoordinates(PreferenceManager.getDefaultSharedPreferences(context).getString("lastLatitude", "55.9449353"),
                    PreferenceManager.getDefaultSharedPreferences(context).getString("lastLongitude", "-3.1839465"));
            getDataNew.setListener(listener);
            getDataNew.execute();
        }else{
            getDataNew.fillSimulatedData();
            getDataNew.setListener(listener);
            getDataNew.simulateExecution();
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (DataSingelton.getInstance().getSensorReadings() != null) {
            Log.v(LOG_TAG, "onP1");
            if (DataSingelton.getInstance().getSensorReadings().size() > 1) {
                Log.v(LOG_TAG, "onPostExecute: " + DataSingelton.getInstance().getSensorReadings().toString());
                Log.v(LOG_TAG, DataSingelton.getInstance().getSensorReadings().get(DataSingelton.getInstance().getSensorReadings().size()-1).toString());
            }
        }
        listener.onDataReady();
    }

    @Override
    protected Void doInBackground(Void... params) {

        double currentLat = Double.parseDouble(latitude);
        Log.v(LOG_TAG, "currentLat: " + currentLat);
        double currentLong = Double.parseDouble(longitude);
        Log.v(LOG_TAG, "currentLong: " + currentLong);

        DataSingelton.getInstance().setSensorCoordinates(AWSClientManager.defaultMobileClient().getDynamoDbManager().getClosestSensorCoordinates(currentLat, currentLong));
        DataSingelton.getInstance().setPollutantThresholds(AWSClientManager.defaultMobileClient().getDynamoDbManager().getPollutantThresholds());
        DataSingelton.getInstance().setPollutantCategoriesInfo(AWSClientManager.defaultMobileClient().getDynamoDbManager().getPollutantCategoryInfo());

        Calendar c = Calendar.getInstance();
        Date from = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        Date to = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) + 1);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String toS = dateFormatter.format(to);
        String fromS = dateFormatter.format(from);


        if(DataSingelton.getInstance().getSensorCoordinates()!=null){
            PreferenceManager.getDefaultSharedPreferences(appContext).edit().putString("closestSourceID", DataSingelton.getInstance().getSensorCoordinates().getSourceID()).commit();
            DataSingelton.getInstance().setSensorReadings(AWSClientManager.defaultMobileClient().getDynamoDbManager().
                    getReadingsBySourceID(DataSingelton.getInstance().getSensorCoordinates().getSourceID(), fromS, toS));
        }
        return null;
    }


    public interface OnDataReceivedListener {

        public void onDataReady();

    }
}
