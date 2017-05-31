package com.minji.librarys.bean;

/**
 * Created by user on 2016/9/6.
 */
public class SeatDetail {

    private String seatName;
    private int seatStatus;
    private int seatId;


    public SeatDetail(String seatName, int seatStatus, int seatId) {
        this.seatName = seatName;
        this.seatStatus = seatStatus;
        this.seatId = seatId;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
}
