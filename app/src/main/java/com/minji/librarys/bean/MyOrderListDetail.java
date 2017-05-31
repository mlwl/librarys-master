package com.minji.librarys.bean;

/**
 * Created by user on 2016/9/9.
 */
public class MyOrderListDetail {

    private String orderPlaceName;
    private String orderTime;
    private String orderStatus;

    public MyOrderListDetail(String orderPlaceName, String orderTime, String orderStatus) {
        this.orderPlaceName = orderPlaceName;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
