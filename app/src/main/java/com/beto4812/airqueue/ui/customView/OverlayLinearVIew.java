package com.beto4812.airqueue.ui.customView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.PollutantThreshold;

/**
 *  Overlay screen to represent pollutant thresholds on top of linear graph
 */
public class OverlayLinearVIew extends  View{

    private String LOG_TAG = "OverOverlayLinearVIew";
    private PollutantThreshold pollutantThreshold;
    private Canvas canvas;
    private int alpha = 50;
    private int renderedPollutantValue = 60;//Max value shown in graph
    private int maxHeight;
    boolean enabled = false;

    public OverlayLinearVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(enabled && pollutantThreshold != null){
            Log.v(LOG_TAG, "onDraw");
            super.onDraw(canvas);
            this.pollutantThreshold = new PollutantThreshold();
            pollutantThreshold.setGreen("0");
            pollutantThreshold.setYellow("10");
            pollutantThreshold.setRed("30");
            pollutantThreshold.setBlack("50");
            pollutantThreshold.setAboveBlack("60");
            this.canvas = canvas;
            //Paint paint = new Paint();
            Paint paint = new Paint();
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            //simulatedHeight = blackPollutantValue*currentHeight/renderedPollutantValue
            //renderedPollutantValue = currentHeight
            //blackPollutantValue = maxHeight
            maxHeight = (int)(pollutantThreshold.getAboveBlackDouble()*getHeight()/renderedPollutantValue);
            Log.v(LOG_TAG, " maxHeight: " + maxHeight);
            int pastHeight=0;

            //green height
            int height = (int)pollutantThreshold.getPercentage("green")*maxHeight/100;
            int color = ContextCompat.getColor(getContext(), R.color.green_traffic_light);
            paint.setColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            canvas.drawRect(0, (getHeight()-height), getWidth(), (getHeight()-height)+height, paint);
            Log.v(LOG_TAG, "green getPercentage: " + pollutantThreshold.getPercentage("green") + " drawHeight: " + height + " canvasHeight: " + getHeight() + " pastHeight: " + pastHeight);
            Log.v(LOG_TAG, "draw: " + "0, "+(getHeight()-height)+", "+getWidth()+", "+((getHeight()-height)+height) );
            pastHeight=(getHeight()-height);


            height = (int)pollutantThreshold.getPercentage("yellow")*maxHeight/100;
            color = ContextCompat.getColor(getContext(), R.color.yellow_traffic_light);
            paint.setColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            canvas.drawRect(0, (getHeight()-height), getWidth(), pastHeight, paint);
            Log.v(LOG_TAG, "yellow getPercentage: " + pollutantThreshold.getPercentage("yellow") + " drawHeight: " + height + " canvasHeight: " + getHeight() + " pastHeight: " + pastHeight);
            Log.v(LOG_TAG, "draw: " + "0, "+(getHeight()-height)+", "+getWidth()+", "+pastHeight);
            pastHeight=(getHeight()-height);


            height = (int)pollutantThreshold.getPercentage("red")*maxHeight/100;
            color = ContextCompat.getColor(getContext(), R.color.red_traffic_light);
            paint.setColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            canvas.drawRect(0, (getHeight()-height), getWidth(), pastHeight, paint);
            Log.v(LOG_TAG, "red getPercentage: " + pollutantThreshold.getPercentage("red") + " drawHeight: " + height + " canvasHeight: " + getHeight() + " pastHeight: " + pastHeight);
            Log.v(LOG_TAG, "draw: " + "0, "+(getHeight()-height)+", "+getWidth()+", "+pastHeight);
            pastHeight=(getHeight()-height);


            height = (int)pollutantThreshold.getPercentage("black")*maxHeight/100;
            color = ContextCompat.getColor(getContext(), R.color.black);
            paint.setColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            canvas.drawRect(0, (getHeight()-height), getWidth(), pastHeight, paint);
            Log.v(LOG_TAG, "black getPercentage: " + pollutantThreshold.getPercentage("black") + " drawHeight: " + height + " canvasHeight: " + getHeight() + " pastHeight: " + pastHeight);
            Log.v(LOG_TAG, "draw: " + "0, "+(getHeight()-height)+", "+getWidth()+", "+pastHeight);
            //getBackground().setAlpha(45);
            pastHeight=(getHeight()-height);
        }
    }

    public void setPollutantThreshold(PollutantThreshold pollutantThreshold){
        this.pollutantThreshold = pollutantThreshold;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        this.invalidate();
    }

    public void setRenderedPollutantValue(int value){
        this.renderedPollutantValue = value;
    }

}
