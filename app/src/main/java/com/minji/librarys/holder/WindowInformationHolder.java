package com.minji.librarys.holder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.base.BaseHolder;
import com.minji.librarys.bean.InformationDetail;
import com.minji.librarys.uitls.ViewsUitls;

/**
 * Created by user on 2016/9/13.
 */
public class WindowInformationHolder extends BaseHolder<InformationDetail> {

    private TextView mMessageTitle;
    private TextView mMessageTime;
    private ImageView imageView;

    @Override
    public View initView() {
        View view = ViewsUitls.inflate(R.layout.item_window_information);

        mMessageTitle = (TextView) view.findViewById(R.id.tv_window_information_item_order_place);
        mMessageTime = (TextView) view.findViewById(R.id.tv_window_information_item_order_time);
        imageView = (ImageView) view.findViewById(R.id.iv_window_information_item_left_image);

        return view;
    }

    @Override
    public void setRelfshData(InformationDetail mData, int postion) {

        mMessageTitle.setText(mData.getTitle());
        mMessageTime.setText(mData.getUpdateTime());

        if (mData.getIsread() == 0) {// 消息已读
            mMessageTime.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.window_message_read));
            mMessageTitle.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.window_message_read));
            imageView.setImageResource(R.mipmap.read_message);
        } else if (mData.getIsread() == 1) {// 消息未读
            mMessageTime.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.black));
            mMessageTitle.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.black));
            imageView.setImageResource(R.mipmap.unread_message);
        }

    }


}
