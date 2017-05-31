package com.minji.librarys.bean;

/**
 * Created by user on 2016/9/9.
 */
public class CancelOrderDetail {

    private String orderPlaceName;
    private String status;
    private String orderTime;
    private int bid;

    public CancelOrderDetail(String orderPlaceName, int bid, String orderTime, String status) {
        this.orderPlaceName = orderPlaceName;
        this.bid = bid;
        this.orderTime = orderTime;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderPlaceName() {
        return orderPlaceName;
    }

    public void setOrderPlaceName(String orderPlaceName) {
        this.orderPlaceName = orderPlaceName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }
}
