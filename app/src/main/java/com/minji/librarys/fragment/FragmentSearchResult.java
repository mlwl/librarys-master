package com.minji.librarys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.uitls.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/9/19.
 */
public class FragmentSearchResult extends Fragment {


    private TextView mobile;
    private TextView time;
    private TextView seatNumber;
    private TextView states;
    private TextView userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_search_seat_result, null);


        mobile = (TextView) view.findViewById(R.id.tv_search_seat_result_right_mobile);
        time = (TextView) view.findViewById(R.id.tv_search_seat_result_right_time);
        seatNumber = (TextView) view.findViewById(R.id.tv_search_seat_result_right_seat_number);
        states = (TextView) view.findViewById(R.id.tv_search_seat_result_right_states);
        userName = (TextView) view.findViewById(R.id.tv_search_seat_result_right_user_name);

        getSearchResultAndSetDate();

        return view;
    }

    private void getSearchResultAndSetDate() {
        String result = SharedPreferencesUtil.getString(getActivity(), StringsFiled.SEARCH_JSON_RESULT, "");
        String seatNumbers = SharedPreferencesUtil.getString(getActivity(), StringsFiled.SEARCH_RESULT_SEAT_NUMBER, "");
        try {
            JSONObject object = new JSONObject(result);
            JSONObject message = object.optJSONObject("message");
            mobile.setText(message.optString("mobile"));
            time.setText(message.optString("time"));
            seatNumber.setText(seatNumbers);
            states.setText(message.optString("states"));
            userName.setText(message.optString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*当hidden为true时说明本Fragment被隐藏,当为false时说明本Fragment显示了*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getSearchResultAndSetDate();
        }
    }

}
