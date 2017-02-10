package com.library;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by user on 26/11/15.
 */
public class NvTab {
     STATES states = STATES.DEFAULT;
    Point point;
    Paint textPaint;
    Paint backgroundPaint;
    Paint borderPaint;
    String text = "x";
    NavigationBar bar;
    int textColor = 0xffffffff;
    int backgroundColor = 0xffff5599;
    int borderColor = 0xffff5599;
    float borderSize=2;
    private Paint.FontMetricsInt metrics = null;

    public NvTab(NavigationBar bar, Point point, String text, float border) {
        this.borderSize=border;
        this.text = text;
        this.bar = bar;
        this.point = point;
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(bar.getTabTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setLinearText(false);
        textPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor );
        backgroundPaint.setStrokeWidth(2);
        backgroundPaint.setStrokeJoin(Paint.Join.BEVEL);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setAntiAlias(true);

       borderPaint = new Paint();
       borderPaint.setColor(borderColor);
       borderPaint.setStrokeWidth(borderSize);
       borderPaint.setStrokeJoin(Paint.Join.BEVEL);
       borderPaint.setStrokeCap(Paint.Cap.ROUND);
       borderPaint.setStyle(Paint.Style.STROKE);
       borderPaint.setAntiAlias(true);


        metrics = textPaint.getFontMetricsInt();
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
    }

    public STATES getStates() {
        return states;
    }

    public void setStates(STATES states) {
        this.states = states;
        switch (states) {
            case DEFAULT:
                setBackgroundColor(bar.getStateColor()[0]);
                setBorderColor(bar.getStateBorderColor()[0]);
                setTextColor(bar.getStateTextColor()[0]);
                break;
            case WRONG:
                setBackgroundColor(bar.getStateColor()[1]);
                setBorderColor(bar.getStateBorderColor()[1]);
                setTextColor(bar.getStateTextColor()[1]);
                break;
            case RIGHT:
                setBackgroundColor(bar.getStateColor()[2]);
                setBorderColor(bar.getStateBorderColor()[2]);
                setTextColor(bar.getStateTextColor()[2]);
                break;
            case SKIPPED:
                setBackgroundColor(bar.getStateColor()[3]);
                setBorderColor(bar.getStateBorderColor()[3]);
                setTextColor(bar.getStateTextColor()[3]);
                break;
            case CURRENT:
                setBackgroundColor(bar.getStateColor()[4]);
                setBorderColor(bar.getStateBorderColor()[4]);
                setTextColor(bar.getStateTextColor()[4]);
                break;
            case PEOCESSED:
                setBackgroundColor(bar.getStateColor()[5]);
                setBorderColor(bar.getStateBorderColor()[5]);
                setTextColor(bar.getStateTextColor()[5]);
                break;
        }

    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
    }

    public void onDrawBG(Canvas canvas, float p)
    {
        if(bar.isBorder())
            canvas.drawCircle(point.getX(), point.getY(), point.getRadius() * p, borderPaint);
        if(!bar.isOnlyBorder())
        canvas.drawCircle(point.getX(), point.getY(), point.getRadius() * p, backgroundPaint);

    }

    public void onDrawFG(Canvas canvas, float p) {
        final int before = -metrics.ascent;
        final int after = metrics.descent + metrics.leading;
        canvas.drawText(text, point.getX(), point.getY() + (before - after) / 2, textPaint);
    }

    public enum STATES {
        DEFAULT, WRONG, RIGHT, SKIPPED, CURRENT, PEOCESSED
    }
}
