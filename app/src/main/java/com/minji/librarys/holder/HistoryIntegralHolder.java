package com.minji.librarys.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.bean.HistoryIntegralDetail;
import com.minji.librarys.uitls.ViewsUitls;

/**
 * Created by user on 2016/9/13.
 */
public class HistoryIntegralHolder extends BaseHolder<HistoryIntegralDetail> {

    private TextView mGetTime;
    private TextView mDescribeGetOrSource;
    private TextView mDescribeMinusOrAdd;
    private TextView mValueMinusOrAdd;
    private TextView mValueGetOrSource;

    @Override
    public View initView() {

        View view = ViewsUitls.inflate(R.layout.item_integral_detail);

        mGetTime = (TextView) view.findViewById(R.id.tv_get_or_expend_integral_time);
        mDescribeGetOrSource = (TextView) view.findViewById(R.id.tv_get_or_point_source_describe);
        mDescribeMinusOrAdd = (TextView) view.findViewById(R.id.tv_minus_or_add_point_describe);
        mValueGetOrSource = (TextView) view.findViewById(R.id.tv_get_or_point_source_value);
        mValueMinusOrAdd = (TextView) view.findViewById(R.id.tv_minus_or_add_point_value);

        return view;
    }

    @Override
    public void setRelfshData(HistoryIntegralDetail mData, int postion) {

        mDescribeGetOrSource.setText("获得积分 : ");
        mDescribeMinusOrAdd.setText(" ; 消耗积分 : ");

        mValueGetOrSource.setText(mData.getAddPoint() + "");
        mValueMinusOrAdd.setText(mData.getMinusPoint() + "");

        mGetTime.setText(mData.getDateTime());
    }

}
