package com.beto4812.airqueue.ui.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CheckPollutantView extends View{

    private static final String LOG_TAG = "CheckPollutantView";
    private int color = Color.BLACK;
    private Canvas canvas;

    public CheckPollutantView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public CheckPollutantView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //canvas.drawColor(Color.WHITE);
        this.canvas = canvas;
        canvas.drawCircle(50, 50, 30, new Paint(color));
    }

    public void setColor(int color){
        this.color = color;
        invalidate();
    }



}
