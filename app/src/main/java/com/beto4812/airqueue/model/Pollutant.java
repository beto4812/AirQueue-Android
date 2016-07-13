package com.beto4812.airqueue.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Encapsulates a pollutant
 */
public class Pollutant implements Parcelable{

    private static final String LOG_TAG = "Pollutant";

    private String code;
    private String value = null;
    private String measureUnit;
    private int category;
    private PollutantThreshold threshold;

    public static final String NITRIC_OXIDE = "NO";
    public static final String NITRIC_OXIDE_NAME = "Nitric Oxide";
    public static final String NITROGEN_DIOXIDE = "NO2";
    public static final String NITROGEN_DIOXIDE_NAME = "Nitrogen Dioxide";
    public static final String OXIDES_OF_NITROGEN = "NOX";
    public static final String OXIDES_OF_NITROGEN_NAME = "Oxides of Nitrogen";
    public static final String PARTICULATE_MATTER_10_MICROMETRE = "PM10";
    public static final String PARTICULATE_MATTER_10_MICROMETRE_NAME = "Particulate Matter 10 micrometre";
    public static final String PARTICULATE_MATTER_2_5_MICROMETRE = "PM2P5";
    public static final String PARTICULATE_MATTER_2_5_MICROMETRE_NAME = "Particulate Matter 2.5 micrometre";
    public static final String PARTICULATE_MATTER_1_MICROMETRE = "PM1";
    public static final String PARTICULATE_MATTER_1_MICROMETRE_NAME = "Particulate Matter 1 micrometre";
    public static final String VOLATILE_PARTICULATE_MATTER_10_MICROMETRE = "VPM10";
    public static final String VOLATILE_PARTICULATE_MATTER_10_MICROMETRE_NAME = "Volatile Particulate Matter 10 micrometre";
    public static final String NON_VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE = "NVPM2P5";
    public static final String NON_VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE_NAME = "Non volatile particulate matter 2.5 micrometre";
    public static final String VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE = "VPM2P5";
    public static final String VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE_NAME = "Volatile particulate matter 2.5 micrometre";
    public static final String SULFUR_DIOXIDE = "SO2";
    public static final String SULFUR_DIOXIDE_NAME = "Sulfur Dioxide";
    public static final String CARBON_MONOXIDE = "CO";
    public static final String CARBON_MONOXIDE_NAME = "Carbon monoxide";
    public static final String OZONE = "O3";
    public static final String OZONE_NAME = "Ozone";

    //Rendered pollutants
    private static final Set<String> RENDERED_POLLUTANTS = new HashSet<>(Arrays.asList(
            new String[] {NITROGEN_DIOXIDE,PARTICULATE_MATTER_10_MICROMETRE, PARTICULATE_MATTER_2_5_MICROMETRE, SULFUR_DIOXIDE, CARBON_MONOXIDE, OZONE}
    ));

    public static final HashMap<String, String> allPollutants(){
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        list.put(NITRIC_OXIDE, NITRIC_OXIDE_NAME);
        list.put(NITROGEN_DIOXIDE, NITROGEN_DIOXIDE_NAME);
        list.put(OXIDES_OF_NITROGEN, OXIDES_OF_NITROGEN_NAME);
        list.put(PARTICULATE_MATTER_10_MICROMETRE, PARTICULATE_MATTER_10_MICROMETRE_NAME);
        list.put(PARTICULATE_MATTER_2_5_MICROMETRE, PARTICULATE_MATTER_2_5_MICROMETRE_NAME);
        list.put(PARTICULATE_MATTER_1_MICROMETRE, PARTICULATE_MATTER_1_MICROMETRE_NAME);
        list.put(SULFUR_DIOXIDE, SULFUR_DIOXIDE_NAME);
        list.put(CARBON_MONOXIDE, CARBON_MONOXIDE_NAME);
        list.put(OZONE, OZONE_NAME);
        return list;
    }

    private static final ArrayList<String> NUMERIC_ID_POLLUTANTS = new ArrayList<String>(Arrays.asList(
            new String[] {NITRIC_OXIDE,
                    OXIDES_OF_NITROGEN,
                    NITROGEN_DIOXIDE,
                    PARTICULATE_MATTER_10_MICROMETRE,
                    PARTICULATE_MATTER_2_5_MICROMETRE,
                    PARTICULATE_MATTER_1_MICROMETRE,
                    SULFUR_DIOXIDE,
                    CARBON_MONOXIDE,
                    OZONE}
    ));

    public Pollutant(String name, String value, String measureUnit) {
        this.code = name;
        this.value = value;
        this.measureUnit = measureUnit;
        setCategory();
    }

    protected Pollutant(Parcel in) {
        code = in.readString();
        value = in.readString();
        measureUnit = in.readString();
    }

    public void setCategory(){
        if(code.contains("PM")){
            this.category = PollutantCategory.PARTICULATE_MATTER;
        }else if(code.startsWith("NO")){
            this.category = PollutantCategory.NITROGEN_OXIDES;
        }else if(code.startsWith("CO")){
            this.category = PollutantCategory.CARBON_MONOXIDE;
        }else{
            this.category = PollutantCategory.OTHER;
        }
    }

    public int getCategory(){
        return this.category;
    }

    public static final Creator<Pollutant> CREATOR = new Creator<Pollutant>() {
        @Override
        public Pollutant createFromParcel(Parcel in) {
            return new Pollutant(in);
        }

        @Override
        public Pollutant[] newArray(int size) {
            return new Pollutant[size];
        }
    };

    public String getCode() {
        return code;
    }


    /**
     *
     * @return numeric ID of the pollutant based on NUMERIC_ID_POLLUTANTS list
     */
    public int pollutantNumericID(){
        return NUMERIC_ID_POLLUTANTS.indexOf(code);
    }

    /**
     * Which pollutants appear on linear graphs
     * @return
     */
    public static HashMap<String, String> getPollutantsInLinearGraph(){
        HashMap<String, String> list = new HashMap<>();
        list.put(NITRIC_OXIDE, NITRIC_OXIDE_NAME);
        //list.put(NITROGEN_DIOXIDE, NITROGEN_DIOXIDE_NAME);
        //list.put(OXIDES_OF_NITROGEN, OXIDES_OF_NITROGEN_NAME);
        list.put(PARTICULATE_MATTER_10_MICROMETRE, PARTICULATE_MATTER_10_MICROMETRE_NAME);
        //list.put(PARTICULATE_MATTER_2_5_MICROMETRE, PARTICULATE_MATTER_2_5_MICROMETRE_NAME);
        //list.put(PARTICULATE_MATTER_1_MICROMETRE, PARTICULATE_MATTER_1_MICROMETRE_NAME);
        //list.put(SULFUR_DIOXIDE, SULFUR_DIOXIDE_NAME);
        //list.put(CARBON_MONOXIDE, CARBON_MONOXIDE_NAME);
        //list.put(OZONE, OZONE_NAME);
        return list;
    }

    public void setName(String name) {
        this.code = name;
    }

    public String getValue() {
        return value;
    }

    public double getDoubleValue(){
        return Double.parseDouble(value);
    }

    public Float getFloatValue(){
        return Float.parseFloat(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(value);
        dest.writeString(measureUnit);
    }

    @Override
    public String toString(){
        return code+"-> v:" + value + " U: " + measureUnit;
    }

    public void setThreshold(PollutantThreshold pollutantThreshold){
        Log.v(LOG_TAG, "setThreshold: "  + pollutantThreshold);
        this.threshold = pollutantThreshold;
    }

    /**
     *
     * @return 1 green, 2 yellow, 3 red, 4 black
     */
    public int getCurrentColorLevel(){
        if(this.getDoubleValue()> Double.parseDouble(threshold.getBlack())){
            return 4;
        }else if(this.getDoubleValue()> Double.parseDouble(threshold.getRed())){
            return 3;
        }else if(this.getDoubleValue()> Double.parseDouble(threshold.getYellow())){
            return 2;
        }else{
            return 1;
        }
    }

    public PollutantThreshold getThresholds(){
        return this.threshold;
    }

    /**
     * Defines if the pollutant is going to be rendered in the pollutants screen
     * @return
     */
    public boolean isRendereable(){
        Log.v(LOG_TAG, "isRendereable: " + RENDERED_POLLUTANTS.contains(code));
        return RENDERED_POLLUTANTS.contains(code);
    }

    public int getThresholdMax(){
        return Integer.parseInt(this.threshold.getBlack());
    }

    public static class PollutantCategory{
        public static final int PARTICULATE_MATTER = 20;
        public static final int NITROGEN_OXIDES = 10;
        public static final int CARBON_MONOXIDE = 30;
        public static final int OTHER = 100;

        public static String getPollutantCategoryString(int category){
            switch (category){
                case OTHER:
                    return "Other Pollutants";
                case PARTICULATE_MATTER:
                    return "Particulate Matter";
                case NITROGEN_OXIDES:
                    return "Nitrogen Oxides";
                case CARBON_MONOXIDE:
                    return "Carbon Monoxide";
                default:
                    return "Undefined";
            }
        }


        public static List<Integer> getCategories(){
            ArrayList<Integer> categories = new ArrayList<>();
            categories.add(PollutantCategory.PARTICULATE_MATTER);
            categories.add(PollutantCategory.NITROGEN_OXIDES);
            categories.add(PollutantCategory.CARBON_MONOXIDE);
            categories.add(PollutantCategory.OTHER);
            return categories;
        }
    }
}
