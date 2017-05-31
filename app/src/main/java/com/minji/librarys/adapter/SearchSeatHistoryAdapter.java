package com.minji.librarys.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.bean.SearchHistory;
import com.minji.librarys.uitls.ViewsUitls;

import java.util.List;

/**
 * Created by user on 2016/8/26.
 */
public class SearchSeatHistoryAdapter extends BaseAdapter {


    private final List<SearchHistory> date;

    public SearchSeatHistoryAdapter(List<SearchHistory> date) {
        this.date = date;
    }

    @Override
    public int getCount() {
        return date.size();
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
        textView.setTextColor(ContextCompat.getColor(ViewsUitls.getContext(), R.color.black));
        textView.setPadding(ViewsUitls.dptopx(0), ViewsUitls.dptopx(6), ViewsUitls.dptopx(6), ViewsUitls.dptopx(6));
        textView.setText(date.get(position).getSearchSeat());

        return textView;
    }
}
