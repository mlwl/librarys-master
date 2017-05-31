package com.minji.librarys.bean;

/**
 * Created by user on 2016/9/13.
 */
public class TodayIntegralDetail {

    private String pointSource;
    private int getPoint;
    private long getTime;

    public TodayIntegralDetail(String pointSource, int getPoint, long getTime) {
        this.pointSource = pointSource;
        this.getPoint = getPoint;
        this.getTime = getTime;
    }

    public String getPointSource() {
        return pointSource;
    }

    public void setPointSource(String pointSource) {
        this.pointSource = pointSource;
    }

    public int getGetPoint() {
        return getPoint;
    }

    public void setGetPoint(int getPoint) {
        this.getPoint = getPoint;
    }

    public long getGetTime() {
        return getTime;
    }

    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }
}
