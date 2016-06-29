package com.beto4812.airqueue.model;


public class PollutantDetail {

    private static final String LOG_TAG = "PollutantDetail";

    private String text;
    private String image;

    public PollutantDetail(String text, String image) {
        this. text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}
