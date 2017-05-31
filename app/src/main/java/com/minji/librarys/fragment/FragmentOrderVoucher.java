package com.minji.librarys.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 2016/10/17.
 */
public class FragmentOrderVoucher extends BaseFragment {

    private String mResultString;
    private List<String> middleList;
    private String mOccupyPerson;
    private String mOccupyTime;
    private String mOccupyStatus;
    private String mOccupySeatName;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_oreder_voucher);

        TextView seatName = (TextView) view.findViewById(R.id.tv_order_voucher_seat_name);
        seatName.setText(mOccupySeatName);
        TextView seatUsePerson = (TextView) view.findViewById(R.id.tv_order_voucher_seat_use_person);
        seatUsePerson.setText(mOccupyPerson);
        TextView seatStatus = (TextView) view.findViewById(R.id.tv_order_voucher_seat_status);
        seatStatus.setText(mOccupyStatus);

        return view;
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        String mUserId = SharedPreferencesUtil.getString(getActivity(), StringsFiled.USERID, "");

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.CANCEL_ORDER_LIST)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                mResultString = response.body().string();
                Log.i("asdfgh", mResultString);
                analysisJsonDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========================onFailure=============================");
            Log.i("asdfgh", "okHttp is request error");
            middleList = null;
        }

        return chat(middleList);
    }

    private void analysisJsonDate() {
        if (!mResultString.contains("html")) {
            try {
                JSONObject object = new JSONObject(mResultString);
                middleList = null;
                middleList = new ArrayList<>();
                if (!object.optString("message").equals("无可用的信息")) {
                    middleList.add("为了让界面不显示为空");
                    mOccupyPerson = object.optString("userName");
                    mOccupyTime = object.optString("time");
                    mOccupyStatus = object.optString("status");
                    mOccupySeatName = object.optString("name");
                }
                System.out.println();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("返回结果为<html>");
            middleList = null;
        }
    }
}
