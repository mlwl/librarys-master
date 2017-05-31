package com.minji.librarys.adapter;

import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.base.MyBaseAdapter;
import com.minji.librarys.bean.HistoryIntegralDetail;
import com.minji.librarys.holder.HistoryIntegralHolder;

import java.util.List;

/**
 * Created by user on 2016/9/13.
 */
public class HistoryIntegralAdapter extends MyBaseAdapter<HistoryIntegralDetail> {

    public HistoryIntegralAdapter(List<HistoryIntegralDetail> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new HistoryIntegralHolder();
    }

    @Override
    public List<HistoryIntegralDetail> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
