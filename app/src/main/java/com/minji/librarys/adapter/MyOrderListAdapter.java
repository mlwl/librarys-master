package com.minji.librarys.adapter;

import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.base.MyBaseAdapter;
import com.minji.librarys.bean.MyOrderListDetail;
import com.minji.librarys.holder.MyOrderListHolder;

import java.util.List;

/**
 * Created by user on 2016/9/9.
 */
public class MyOrderListAdapter extends MyBaseAdapter<MyOrderListDetail> {

    public MyOrderListAdapter(List<MyOrderListDetail> list) {
        super(list);
    }

    @Override
    public BaseHolder<MyOrderListDetail> getHolder() {
        return new MyOrderListHolder();
    }

    @Override
    public List<MyOrderListDetail> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
