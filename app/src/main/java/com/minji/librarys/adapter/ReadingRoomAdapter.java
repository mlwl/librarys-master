package com.minji.librarys.adapter;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.bean.SeatDetail;
import com.minji.librarys.uitls.ViewsUitls;

import java.util.List;

/**
 * Created by user on 2016/8/22.
 */
public class ReadingRoomAdapter extends BaseAdapter {

    private final List<SeatDetail> readingRoomSeatses;

    public ReadingRoomAdapter(List<SeatDetail> readingRoomSeatses) {
        this.readingRoomSeatses = readingRoomSeatses;
    }

    @Override
    public int getCount() {
        return readingRoomSeatses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = new TextView(ViewsUitls.getContext());
        textView.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.white));
        textView.setPadding(ViewsUitls.dptopx(6), ViewsUitls.dptopx(6), ViewsUitls.dptopx(6), ViewsUitls.dptopx(6));
        textView.setText(readingRoomSeatses.get(position).getSeatName());
        // 设置字体加粗
//        TextPaint textPaint = textView.getPaint();
//        textPaint.setFakeBoldText(true);

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(ViewsUitls.dptopx(9));

        if (readingRoomSeatses.get(position).getSeatStatus() == 0) {
            textView.setBackgroundResource(R.drawable.shape_select_area_reading_room_seat_state_free);
        } else if (readingRoomSeatses.get(position).getSeatStatus() == 1) {
            textView.setBackgroundResource(R.drawable.shape_select_area_reading_room_seat_state_occupation);
        }

        textView.setSingleLine(true);




        return textView;
    }
}
