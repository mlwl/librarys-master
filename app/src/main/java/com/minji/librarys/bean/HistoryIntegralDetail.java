package com.minji.librarys.bean;

/**
 * Created by user on 2016/9/13.
 */
public class HistoryIntegralDetail {

    private int addPoint;
    private double  minusPoint;
    private String dateTime;

    public HistoryIntegralDetail(int addPoint, double minusPoint, String dateTime) {
        this.addPoint = addPoint;
        this.minusPoint = minusPoint;
        this.dateTime = dateTime;
    }

    public int getAddPoint() {
        return addPoint;
    }

    public void setAddPoint(int addPoint) {
        this.addPoint = addPoint;
    }

    public double getMinusPoint() {
        return minusPoint;
    }

    public void setMinusPoint(double minusPoint) {
        this.minusPoint = minusPoint;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
