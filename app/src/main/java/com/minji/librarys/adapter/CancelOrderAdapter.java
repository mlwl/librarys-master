package com.minji.librarys.adapter;

import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.base.MyBaseAdapter;
import com.minji.librarys.bean.CancelOrderDetail;
import com.minji.librarys.holder.CancelOrderHolder;

import java.util.List;

/**
 * Created by user on 2016/9/9.
 */
public class CancelOrderAdapter extends MyBaseAdapter<CancelOrderDetail> {

    public CancelOrderAdapter(List<CancelOrderDetail> list) {
        super(list);
    }

    @Override
    public BaseHolder<CancelOrderDetail> getHolder() {
        return new CancelOrderHolder();
    }

    @Override
    public List<CancelOrderDetail> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
