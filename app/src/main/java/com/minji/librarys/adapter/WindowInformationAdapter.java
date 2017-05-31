package com.minji.librarys.adapter;

import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.base.MyBaseAdapter;
import com.minji.librarys.bean.InformationDetail;
import com.minji.librarys.holder.WindowInformationHolder;

import java.util.List;

/**
 * Created by user on 2016/9/13.
 */
public class WindowInformationAdapter extends MyBaseAdapter<InformationDetail> {

    public WindowInformationAdapter(List<InformationDetail> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new WindowInformationHolder();
    }

    @Override
    public List<InformationDetail> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
