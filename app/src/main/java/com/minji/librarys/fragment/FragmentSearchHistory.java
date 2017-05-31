package com.minji.librarys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.SearchSeatHistoryAdapter;
import com.minji.librarys.bean.SearchHistory;
import com.minji.librarys.ui.SearchSeatActivity;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/19.
 */
public class FragmentSearchHistory extends Fragment {

    private SearchSeatHistoryAdapter searchSeatHistoryAdapter;
    private SearchSeatActivity searchSeatActivity;

    private List<SearchHistory> mSearchHistories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.layout_search_history, null);

        searchSeatActivity = (SearchSeatActivity) getActivity();

        GridView mShowHistory = (GridView) inflate.findViewById(R.id.gv_search_seat_history);

        // 获取数据并显示到GridView
        getSearchHistory();

        searchSeatHistoryAdapter = new SearchSeatHistoryAdapter(mSearchHistories);
        mShowHistory.setAdapter(searchSeatHistoryAdapter);

        mShowHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view;
                searchSeatActivity.getInputSearchSeat().setText(textView.getText().toString().trim());
                searchSeatActivity.requestSearchSeat(textView.getText().toString().trim());
            }
        });


        TextView cleanHistory = (TextView) inflate.findViewById(R.id.tv_search_seat_bottom_history_clean_all);
        cleanHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.saveStirng(getActivity(), StringsFiled.SEARCH_HISTORY, "");
                searchSeatActivity.hideSearchHistory();
            }
        });

        return inflate;
    }

    private void getSearchHistory() {
        String searchHistory = SharedPreferencesUtil.getString(getActivity(), StringsFiled.SEARCH_HISTORY, "");
        if (!StringUtils.isEmpty(searchHistory)) {
            analysisGSONDate(searchHistory);
        }
    }

    /*当hidden为true时说明本Fragment被隐藏,当为false时说明本Fragment显示了*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

            getSearchHistory();

            if (mSearchHistories != null && searchSeatHistoryAdapter != null) {
                searchSeatHistoryAdapter.notifyDataSetChanged();// TODO 在更新adapter时数据只能添加删除不能整个数据换成另一个实例数据
            }
        }
    }

    private void analysisGSONDate(String searchHistory) {
        if (mSearchHistories == null) {
            mSearchHistories = new ArrayList<>();
        }
        try {
            JSONArray jsonArray = new JSONArray(searchHistory);
            mSearchHistories.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                mSearchHistories.add(new SearchHistory(object.optString("searchSeat")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
