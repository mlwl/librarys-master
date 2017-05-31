package com.minji.librarys.adapter;

import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.base.MyBaseAdapter;
import com.minji.librarys.bean.TodayIntegralDetail;
import com.minji.librarys.holder.TodayIntegralHolder;

import java.util.List;

/**
 * Created by user on 2016/9/13.
 */
public class TodayIntegralAdapter extends MyBaseAdapter<TodayIntegralDetail> {

    public TodayIntegralAdapter(List<TodayIntegralDetail> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new TodayIntegralHolder();
    }

    @Override
    public List<TodayIntegralDetail> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
