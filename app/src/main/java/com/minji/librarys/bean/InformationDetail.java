package com.minji.librarys.bean;

/**
 * Created by user on 2016/10/17.
 */
public class InformationDetail {

    public int id;
    public int isread;
    public String title;
    public String updateTime;

    public InformationDetail(int id, int isread, String title, String updateTime) {
        this.id = id;
        this.isread = isread;
        this.title = title;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
