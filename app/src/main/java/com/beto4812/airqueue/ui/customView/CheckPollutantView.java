package com.beto4812.airqueue.ui.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.ui.main.pollutantsLinear.viewHolder.MultiplePollutantLinearViewHolder;

public class CheckPollutantView extends View {

    private static final String LOG_TAG = "CheckPollutantView";
    private int originalColor;
    private int color = Color.BLACK;
    private int radius = 60;
    private int glowRadius = 15;
    private Canvas canvas;
    private Pollutant pollutant;
    private String pollutantCode;
    private Paint radialPaint;
    private boolean disabled = false;
    private boolean selected = false;
    private TextView textView;
    private MultiplePollutantLinearViewHolder.OnPollutantSelectedListener listener;

    public CheckPollutantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(LOG_TAG, "onDraw");
        super.onDraw(canvas);
        this.canvas = canvas;
        Paint paint = new Paint();
        radialPaint = new Paint();

        radialPaint.setStrokeWidth(1);
        radialPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        radialPaint.setShader(new RadialGradient(getWidth() / 2, getHeight() / 2,
                getHeight() / 3, Color.WHITE, color, Shader.TileMode.CLAMP));

        paint.setColor(Color.YELLOW);
        if(selected){
            canvas.drawCircle(radius, radius, radius, paint);
        }

        canvas.drawCircle(radius, radius, radius-glowRadius, radialPaint);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);

        canvas.drawText(pollutantCode, 0, radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG, "touch");
        if(!disabled){
            selected = !selected;
            if(selected){
                listener.onPollutantSelected(pollutantCode);
                textView.setText("Selected");
            }else{
                listener.onPollutantDeSelected(pollutantCode);
                textView.setText("");
            }
        }
        invalidate();
        return false;
    }

    public void setColor(int color) {
        this.originalColor = color;
        this.color = color;
        invalidate();
    }

    public void setPollutantCode(String code){
        this.pollutantCode = code;
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    /**
     * If there is no data its disabled and it cant be selected.
     */
    public void setDisabled(){
        Log.v(LOG_TAG, pollutantCode +" setDisabled");

        textView.setText("No data");
        this.color = Color.rgb(128, 128, 128);
        this.disabled = true;
        invalidate();
    }

    public void setEnabled(){
        color = originalColor;
        textView.setText("");
        this.disabled = false;
        invalidate();
    }

    public void setSelected(){
        this.selected = true;
    }

    public void setListener(MultiplePollutantLinearViewHolder.OnPollutantSelectedListener listener){
        this.listener = listener;
    }

    public void setPollutant(Pollutant pollutant){
        this.pollutant = pollutant;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        return measurement;
    }

}
