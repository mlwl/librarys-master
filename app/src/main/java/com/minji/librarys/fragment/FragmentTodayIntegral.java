package com.minji.librarys.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.TodayIntegralAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.TodayIntegralDetail;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONArray;
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
 * Created by user on 2016/9/12.
 */
public class FragmentTodayIntegral extends BaseFragment<TodayIntegralDetail> {

    private String mResultString;
    private List<TodayIntegralDetail> todayIntegralDetails;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_normal_list);
        ListView listView = (ListView) view.findViewById(R.id.lv_normal_list);

        TodayIntegralAdapter todayIntegralAdapter = new TodayIntegralAdapter(todayIntegralDetails);

        listView.setAdapter(todayIntegralAdapter);


        return view;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        String mUserId = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.USERID, "");
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.TODAY_INTEGRAL)
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
            Log.i("失败", "okHttp is request error");
            todayIntegralDetails = null;
        }
        return chat(todayIntegralDetails);
    }

    private void analysisJsonDate() {
        try {
            JSONObject object = new JSONObject(mResultString);

            todayIntegralDetails = new ArrayList<>();
            if (object.has("userPointDayInfoList")) {
                JSONArray jsonArray = object.optJSONArray("userPointDayInfoList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    JSONObject getTime = jsonObject.optJSONObject("getTime");
                    todayIntegralDetails.add(new TodayIntegralDetail(jsonObject.optString("pointSource"), jsonObject.optInt("getPoint"), getTime.optLong("time")));
                }
            }
            System.out.println();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
