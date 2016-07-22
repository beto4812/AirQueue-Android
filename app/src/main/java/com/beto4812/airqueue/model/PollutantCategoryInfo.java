package com.beto4812.airqueue.model;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSConstants;

/**
 * Encapsulates information about a pollutant category
 */
@DynamoDBTable(tableName = AWSConstants.POLLUTANT_CATEGORY_INFO_TABLE_NAME)
public class PollutantCategoryInfo {

    private static final String LOG_TAG = "PollutantCategoryInfo";

    //String pollutantCode;
    private int pollutantCategory;
    private String url;
    private String code;

    int images[] = {R.drawable.air1,
            R.drawable.air2,
            R.drawable.air3,
            R.drawable.air4,
            R.drawable.air1,
            R.drawable.air2,
            R.drawable.air3,
            R.drawable.air4};

    public String getPollutantCategoryString(){
        return Pollutant.PollutantCategory.getPollutantCategoryString(pollutantCategory);
    }

    public int getPollutantCategory() {
        return pollutantCategory;
    }

    public void setPollutantCategory(int pollutantCategory) {
        this.pollutantCategory = pollutantCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage(){
        //Log.v(LOG_TAG, "getImage: " + Pollutant.PollutantCategory.getCategories().indexOf(pollutantCategory) + " pollutantCategory: " + pollutantCategory);
        return images[Pollutant.PollutantCategory.getCategories().indexOf(pollutantCategory)];
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PollutantCategoryInfo{" +
                "url='" + url + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public PollutantCategoryInfo() {
    }
}
