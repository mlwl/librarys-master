package com.minji.librarys.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.librarys.ObserverTag;
import com.minji.librarys.R;
import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.bean.CancelOrderDetail;
import com.minji.librarys.observer.MySubject;
import com.minji.librarys.uitls.ViewsUitls;

/**
 * Created by user on 2016/9/9.
 */
public class CancelOrderHolder extends BaseHolder<CancelOrderDetail> {

    private TextView mOrderPlace;
    private TextView mOrderTime;
    private TextView mCancelButton;

    @Override
    public View initView() {
        View view = ViewsUitls.inflate(R.layout.item_cancel_order);

        mOrderPlace = (TextView) view.findViewById(R.id.tv_cancel_order_item_order_place);
        mOrderTime = (TextView) view.findViewById(R.id.tv_cancel_order_item_order_time);
        mCancelButton = (TextView) view.findViewById(R.id.tv_cancel_order_item_cancel_button);

        return view;
    }

    @Override
    public void setRelfshData(final CancelOrderDetail mData, final int postion) {
        mOrderPlace.setText(mData.getOrderPlaceName());
        mOrderTime.setText(mData.getOrderTime());

        mCancelButton.setText(mData.getStatus());
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cancelOrderBid = mData.getBid();
                MySubject.getInstance().operation(postion, ObserverTag.ItemCancelOrderButton, cancelOrderBid);
            }
        });

    }

}
