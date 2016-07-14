package com.beto4812.airqueue.ui.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.beto4812.airqueue.model.Pollutant;

public class CheckPollutantView extends View {

    private static final String LOG_TAG = "CheckPollutantView";
    private int color = Color.BLACK;
    private int radius = 50;
    private Canvas canvas;
    private Pollutant pollutant;

    public CheckPollutantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckPollutantView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(LOG_TAG, "onDraw");

        super.onDraw(canvas);
        //canvas.drawColor(Color.WHITE);
        this.canvas = canvas;
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(radius, radius, radius, paint);
    }

    public void setColor(int color) {
        Log.v(LOG_TAG, "setColor: " + color);
        this.color = color;
        invalidate();
    }

    public void setPollutant(Pollutant pollutant){
        this.pollutant = pollutant;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(LOG_TAG, "onMeasure: width: " + MeasureSpec.getSize(widthMeasureSpec) + " , height: "
                + MeasureSpec.getSize(heightMeasureSpec)+ " widthMode: "+MeasureSpec.getMode(widthMeasureSpec) + " heightMode: "+MeasureSpec.getMode(heightMeasureSpec));
        setMeasuredDimension(getMeasurement(widthMeasureSpec, radius*2), getMeasurement(heightMeasureSpec, radius*2));
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                // This means the width of this view has been given.
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                // Take the minimum of the preferred size and what
                // we were told to be.
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        Log.v(LOG_TAG , " reurn measurement: " + measurement);
        return measurement;
    }

}
