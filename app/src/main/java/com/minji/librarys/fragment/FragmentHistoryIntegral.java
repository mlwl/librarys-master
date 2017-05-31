package com.minji.librarys.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.HistoryIntegralAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.HistoryIntegralDetail;
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
public class FragmentHistoryIntegral extends BaseFragment<HistoryIntegralDetail> {

    private String mResultString;
    private List<HistoryIntegralDetail> historyIntegralDetails;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_normal_list);
        ListView listView = (ListView) view.findViewById(R.id.lv_normal_list);

        HistoryIntegralAdapter historyIntegralAdapter = new HistoryIntegralAdapter(historyIntegralDetails);

        listView.setAdapter(historyIntegralAdapter);

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
                .url(address + IpFiled.HISTORY_INTEGRAL)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                mResultString = response.body().string();
                Log.i("成功", mResultString);
                analysisJsonDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========================onFailure=============================");
            Log.i("失败", "okHttp is request error");
            historyIntegralDetails = null;
        }
        return chat(historyIntegralDetails);
    }

    private void analysisJsonDate() {
        try {
            JSONObject object = new JSONObject(mResultString);
            historyIntegralDetails = new ArrayList<>();
            if (object.has("pointCensusList")) {
                JSONArray pointCensusList = object.optJSONArray("pointCensusList");
                for (int i = 0; i < pointCensusList.length(); i++) {
                    JSONObject jsonObject = pointCensusList.optJSONObject(i);
                    historyIntegralDetails.add(new HistoryIntegralDetail(jsonObject.optInt("addpoint"), jsonObject.optDouble("minuspoint"), jsonObject.optString("datetime")));
                }
            }
            System.out.println();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
