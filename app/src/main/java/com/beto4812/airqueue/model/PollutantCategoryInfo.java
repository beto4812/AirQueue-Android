package com.beto4812.airqueue.model;

import com.beto4812.airqueue.R;
/**
 * Encapsulates information about a pollutant category
 */
public class PollutantCategoryInfo {

    //String pollutantCode;
    int pollutantCategory;
    String info;
    int images[] = {R.drawable.air1,
            R.drawable.air2,
            R.drawable.air3,
            R.drawable.air4,
            R.drawable.air1,
            R.drawable.air2,
            R.drawable.air3,
            R.drawable.air4};

    public PollutantCategoryInfo(int pollutantCategory) {
        this.pollutantCategory = pollutantCategory;
    }

    public String getPollutantCategoryString(){
        return Pollutant.PollutantCategory.getPollutantCategoryString(pollutantCategory);
    }

    public int getPollutantCategory() {
        return pollutantCategory;
    }

    public void setPollutantCategory(int pollutantCategory) {
        this.pollutantCategory = pollutantCategory;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImage(){
        return images[Pollutant.PollutantCategory.getCategories().indexOf(pollutantCategory)];
    }
}
