package com.minji.librarys.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.bean.MyOrderListDetail;
import com.minji.librarys.uitls.ViewsUitls;

/**
 * Created by user on 2016/9/9.
 */
public class MyOrderListHolder extends BaseHolder<MyOrderListDetail> {
    private TextView mOrderPlace;
    private TextView mOrderTime;
    private TextView mOrderStatus;

    @Override
    public View initView() {
        View view = ViewsUitls.inflate(R.layout.item_cancel_order);

        mOrderPlace = (TextView) view.findViewById(R.id.tv_cancel_order_item_order_place);
        mOrderTime = (TextView) view.findViewById(R.id.tv_cancel_order_item_order_time);
        mOrderStatus = (TextView) view.findViewById(R.id.tv_cancel_order_item_cancel_button);

        return view;
    }

    @Override
    public void setRelfshData(MyOrderListDetail mData, int postion) {

        mOrderPlace.setText(mData.getOrderPlaceName());
        mOrderTime.setText(mData.getOrderTime());

        String orderStatus = mData.getOrderStatus();
        if (orderStatus.equals("预约中")) {
            mOrderStatus.setBackgroundResource(R.drawable.shape_select_area_reading_room_seat_state_free);
        } else if (orderStatus.equals("已释放")) {
            mOrderStatus.setBackgroundResource(R.drawable.shape_select_area_reading_room_seat_state_occupation);
        }
        mOrderStatus.setText(orderStatus);

    }

}
